package com.example.seagri.infra.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.example.seagri.infra.dto.GlosaCgeRowDTO;
import com.example.seagri.infra.dto.GlosaSeadRowDTO;
import com.example.seagri.infra.integrity.HashService;
import com.example.seagri.infra.model.GlosaRecord;
import com.example.seagri.infra.model.Supply;
import com.example.seagri.infra.repository.GlosaRepository;
import com.example.seagri.infra.repository.SupplyRepository;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.util.JRSaver;

/**
 * Gera os 3 relatórios governamentais do Motor de Glosa (AP 04):
 *   - SEAD: Liquidação financeira com desconto de glosa
 *   - CGE:  Compliance — transações GLOSADO/ALERTA com evidências e hashes
 *   - TCE:  ROI mensal consolidado com certificado de integridade
 */
@Service
public class GlosaReportService {

    private static final DateTimeFormatter FMT_DATA  = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final DateTimeFormatter FMT_MES   = DateTimeFormatter.ofPattern("MM/yyyy");
    private static final DateTimeFormatter FMT_STAMP = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    @Autowired private GlosaRepository   glosaRepository;
    @Autowired private SupplyRepository  supplyRepository;
    @Autowired private HashService       hashService;

    // ── SEAD — Liquidação ────────────────────────────────────────────────────

    public byte[] gerarRelatorioSead(LocalDate mes, String generatedBy)
            throws FileNotFoundException, JRException {

        LocalDateTime inicio = mes.withDayOfMonth(1).atStartOfDay();
        LocalDateTime fim    = mes.withDayOfMonth(mes.lengthOfMonth()).atTime(23, 59, 59);

        List<Supply> abastecimentos = supplyRepository.getAllSupplies(inicio, fim);

        // Mapa transacaoId → GlosaRecord para cruzamento
        List<String> codigos = abastecimentos.stream()
                .map(Supply::getCod_transaction)
                .filter(c -> c != null)
                .toList();
        Map<String, GlosaRecord> glosaMap = codigos.isEmpty() ? Map.of()
                : glosaRepository.findByTransacaoIdIn(codigos).stream()
                        .filter(g -> "GLOSADO".equals(g.getGlosaStatus()))
                        .collect(Collectors.toMap(GlosaRecord::getTransacaoId, g -> g, (a, b) -> a));

        List<GlosaSeadRowDTO> linhas = new ArrayList<>();
        linhas.add(new GlosaSeadRowDTO()); // linha vazia obrigatória (ver workaround Jasper)

        BigDecimal totalBruto   = BigDecimal.ZERO;
        BigDecimal totalGlosado = BigDecimal.ZERO;

        for (Supply s : abastecimentos) {
            BigDecimal bruto  = s.getEmission_value() != null ? s.getEmission_value() : BigDecimal.ZERO;
            boolean isGlosado = s.getCod_transaction() != null && glosaMap.containsKey(s.getCod_transaction());
            BigDecimal glosado = isGlosado ? bruto : BigDecimal.ZERO;
            BigDecimal liquido = bruto.subtract(glosado);

            totalBruto   = totalBruto.add(bruto);
            totalGlosado = totalGlosado.add(glosado);

            linhas.add(new GlosaSeadRowDTO(
                    s.getLicense_plate(),
                    s.getTransaction_date() != null ? s.getTransaction_date().format(FMT_DATA) : "",
                    fmt(bruto), fmt(glosado), fmt(liquido)));
        }

        BigDecimal totalLiquido = totalBruto.subtract(totalGlosado);
        String sealTimestamp    = LocalDateTime.now().format(FMT_STAMP);
        String sealInput        = "SEAD|" + mes + "|" + fmt(totalBruto) + "|" + fmt(totalGlosado) + "|" + generatedBy + "|" + sealTimestamp;
        String sealHash         = hashService.generate(sealInput);

        File file = ResourceUtils.getFile("classpath:reportsFile/seadReport.jrxml");
        String path = file.getParent();
        JasperReport jr = compilar(path, "seadReport");

        Map<String, Object> params = new HashMap<>();
        params.put("mesReferencia",   mes.format(FMT_MES));
        params.put("totalBruto",      fmt(totalBruto));
        params.put("totalGlosado",    fmt(totalGlosado));
        params.put("totalLiquido",    fmt(totalLiquido));
        params.put("sealHash",        sealHash);
        params.put("sealTimestamp",   sealTimestamp);
        params.put("sealGeneratedBy", generatedBy);
        params.put("generationDate",  new Date().toString());

        JasperPrint print = JasperFillManager.fillReport(jr, params,
                new JRBeanCollectionDataSource(linhas));
        return JasperExportManager.exportReportToPdf(print);
    }

    // ── CGE — Compliance ─────────────────────────────────────────────────────

    public byte[] gerarRelatorioCge(LocalDate mes, String generatedBy)
            throws FileNotFoundException, JRException {

        LocalDateTime inicio = mes.withDayOfMonth(1).atStartOfDay();
        LocalDateTime fim    = mes.withDayOfMonth(mes.lengthOfMonth()).atTime(23, 59, 59);

        List<GlosaRecord> registros = glosaRepository.findByStatusInAndPeriod(
                List.of("GLOSADO", "ALERTA"), inicio, fim);

        List<GlosaCgeRowDTO> linhas = new ArrayList<>();
        linhas.add(new GlosaCgeRowDTO()); // workaround Jasper

        for (GlosaRecord g : registros) {
            String evidencia = construirEvidencia(g);
            linhas.add(new GlosaCgeRowDTO(
                    g.getPlaca(),
                    evidencia,
                    g.getObservacao(),
                    g.getIntegrityHash()));
        }

        String sealTimestamp = LocalDateTime.now().format(FMT_STAMP);
        String sealInput     = "CGE|" + mes + "|" + registros.size() + "|" + generatedBy + "|" + sealTimestamp;
        String sealHash      = hashService.generate(sealInput);

        File file = ResourceUtils.getFile("classpath:reportsFile/cgeReport.jrxml");
        String path = file.getParent();
        JasperReport jr = compilar(path, "cgeReport");

        Map<String, Object> params = new HashMap<>();
        params.put("mesReferencia",   mes.format(FMT_MES));
        params.put("totalRegistros",  String.valueOf(registros.size()));
        params.put("sealHash",        sealHash);
        params.put("sealTimestamp",   sealTimestamp);
        params.put("sealGeneratedBy", generatedBy);
        params.put("generationDate",  new Date().toString());

        JasperPrint print = JasperFillManager.fillReport(jr, params,
                new JRBeanCollectionDataSource(linhas));
        return JasperExportManager.exportReportToPdf(print);
    }

    // ── TCE — ROI Mensal ─────────────────────────────────────────────────────

    public byte[] gerarRelatorioTce(LocalDate mes, String generatedBy)
            throws FileNotFoundException, JRException {

        LocalDateTime inicio = mes.withDayOfMonth(1).atStartOfDay();
        LocalDateTime fim    = mes.withDayOfMonth(mes.lengthOfMonth()).atTime(23, 59, 59);

        long   totalVeiculos  = glosaRepository.countDistinctPlacaByPeriod(inicio, fim);
        BigDecimal gastoBruto = nvl(glosaRepository.sumValorBrutoByPeriod(inicio, fim));
        BigDecimal economiaReal = nvl(glosaRepository.sumValorGlosadoByPeriod(inicio, fim));
        BigDecimal gastoliquido = gastoBruto.subtract(economiaReal);

        // Certificado de integridade: hash encadeado de todos os hashes do período
        List<String> hashes = glosaRepository.findAllHashesByPeriod(inicio, fim);
        String certificadoInput = String.join("|", hashes) + "|" + mes + "|" + generatedBy;
        String certificadoHash  = hashService.generate(certificadoInput);

        String sealTimestamp = LocalDateTime.now().format(FMT_STAMP);
        String sealInput     = "TCE|" + mes + "|" + certificadoHash + "|" + generatedBy + "|" + sealTimestamp;
        String sealHash      = hashService.generate(sealInput);

        File file = ResourceUtils.getFile("classpath:reportsFile/tceReport.jrxml");
        String path = file.getParent();
        JasperReport jr = compilar(path, "tceReport");

        Map<String, Object> params = new HashMap<>();
        params.put("mesReferencia",    mes.format(FMT_MES));
        params.put("totalVeiculos",    String.valueOf(totalVeiculos));
        params.put("gastoBruto",       fmt(gastoBruto));
        params.put("economiaReal",     fmt(economiaReal));
        params.put("gastoLiquido",     fmt(gastoliquido));
        params.put("totalTransacoes",  String.valueOf(hashes.size()));
        params.put("certificadoHash",  certificadoHash);
        params.put("sealHash",         sealHash);
        params.put("sealTimestamp",    sealTimestamp);
        params.put("sealGeneratedBy",  generatedBy);
        params.put("generationDate",   new Date().toString());

        JasperPrint print = JasperFillManager.fillReport(jr, params, new JREmptyDataSource());
        return JasperExportManager.exportReportToPdf(print);
    }

    // ── Utilitários ──────────────────────────────────────────────────────────

    private JasperReport compilar(String path, String nome) throws JRException {
        JasperReport jr = JasperCompileManager.compileReport(path + "/" + nome + ".jrxml");
        JRSaver.saveObject(jr, path + "/" + nome + ".jasper");
        return jr;
    }

    private String construirEvidencia(GlosaRecord g) {
        if (g.getPostoLat() != null && g.getPostoLng() != null) {
            return "Posto: " + g.getPostoLat() + ", " + g.getPostoLng();
        }
        return "Coordenadas não disponíveis";
    }

    private String fmt(BigDecimal v) {
        return v == null ? "R$ 0,00" : "R$ " + String.format("%.2f", v).replace(".", ",");
    }

    private BigDecimal nvl(BigDecimal v) {
        return v == null ? BigDecimal.ZERO : v;
    }
}
