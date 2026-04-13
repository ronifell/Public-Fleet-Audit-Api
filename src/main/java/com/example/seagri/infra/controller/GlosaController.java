package com.example.seagri.infra.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.seagri.infra.config.UserProfile;
import com.example.seagri.infra.dto.GlosaDashboardDTO;
import com.example.seagri.infra.dto.GlosaResultDTO;
import com.example.seagri.infra.dto.GlosaTransactionInputDTO;
import com.example.seagri.infra.service.GlosaImportService;
import com.example.seagri.infra.service.GlosaReportService;
import com.example.seagri.infra.service.GlosaService;

import net.sf.jasperreports.engine.JRException;

/**
 * API do Motor de Glosa (AP 04).
 *
 * Endpoints:
 *   POST /glosa/processar         — recebe lista de transações (JSON) e executa as regras
 *   POST /glosa/importar          — recebe arquivo CSV ou Excel (.xlsx) e executa as regras
 *   GET  /glosa/registros         — lista todos os registros de Fé Pública
 *   GET  /glosa/registros/{placa} — filtra registros por placa
 *   GET  /glosa/dashboard         — dashboard de ROI e conformidade
 */
@RestController
@RequestMapping("/glosa")
public class GlosaController {

    @Autowired
    private GlosaService service;

    @Autowired
    private GlosaImportService importService;

    @Autowired
    private GlosaReportService reportService;

    private String currentUserLogin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserProfile up) {
            return up.user.getUserName();
        }
        return "system";
    }

    /**
     * Recebe um array de transações da operadora, aplica as 3 regras de glosa
     * e persiste os resultados na tabela Append-Only de Fé Pública.
     *
     * Exemplo de payload: ver GlosaTransactionInputDTO
     */
    @PostMapping("/processar")
    public ResponseEntity<List<GlosaResultDTO>> processar(
            @RequestBody List<GlosaTransactionInputDTO> transacoes) {
        List<GlosaResultDTO> resultados = service.processar(transacoes, currentUserLogin());
        return ResponseEntity.ok(resultados);
    }

    /**
     * Importa um arquivo CSV ou Excel (.xlsx) enviado pela operadora.
     * Detecta automaticamente o formato pelo Content-Type ou extensão do arquivo.
     *
     * Colunas esperadas no arquivo (cabeçalho obrigatório na 1ª linha):
     *   transacao_id | timestamp | placa | posto_cnpj | posto_lat | posto_lng |
     *   combustivel  | volume_litros | odometro_informado | valor_total* |
     *   veiculo_lat* | veiculo_lng*
     *
     * Colunas marcadas com * são opcionais.
     */
    @PostMapping(value = "/importar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> importar(@RequestParam("arquivo") MultipartFile arquivo) {
        if (arquivo.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("erro", "Arquivo vazio."));
        }

        String nome = arquivo.getOriginalFilename() != null
                ? arquivo.getOriginalFilename().toLowerCase() : "";
        String contentType = arquivo.getContentType() != null
                ? arquivo.getContentType().toLowerCase() : "";

        try {
            List<GlosaTransactionInputDTO> transacoes;

            if (nome.endsWith(".xlsx") || contentType.contains("spreadsheetml")
                    || contentType.contains("excel")) {
                transacoes = importService.parseExcel(arquivo);
            } else if (nome.endsWith(".csv") || contentType.contains("csv")
                    || contentType.contains("text/plain")) {
                transacoes = importService.parseCsv(arquivo);
            } else {
                return ResponseEntity.badRequest().body(
                        Map.of("erro", "Formato não suportado. Envie um arquivo .csv ou .xlsx"));
            }

            if (transacoes.isEmpty()) {
                return ResponseEntity.badRequest().body(
                        Map.of("erro", "Nenhuma transação válida encontrada no arquivo."));
            }

            List<GlosaResultDTO> resultados = service.processar(transacoes, currentUserLogin());
            return ResponseEntity.ok(Map.of(
                    "arquivo",       arquivo.getOriginalFilename(),
                    "totalLidas",    transacoes.size(),
                    "totalProcessadas", resultados.size(),
                    "resultados",    resultados));

        } catch (IOException e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("erro", "Falha ao ler o arquivo: " + e.getMessage()));
        }
    }

    /**
     * Lista todos os registros de glosa (tabela de Fé Pública).
     */
    @GetMapping("/registros")
    public ResponseEntity<List<GlosaResultDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    /**
     * Lista todos os registros de glosa de uma placa específica.
     */
    @GetMapping("/registros/{placa}")
    public ResponseEntity<List<GlosaResultDTO>> listarPorPlaca(@PathVariable String placa) {
        return ResponseEntity.ok(service.listarPorPlaca(placa));
    }

    /**
     * Dashboard de ROI: totais por status, valor glosado e % de conformidade.
     */
    @GetMapping("/dashboard")
    public ResponseEntity<GlosaDashboardDTO> dashboard() {
        return ResponseEntity.ok(service.dashboard());
    }

    // ── Relatórios Governamentais ────────────────────────────────────────────

    private LocalDate parseMes(String mes) {
        try {
            return YearMonth.parse(mes).atDay(1);
        } catch (DateTimeParseException e) {
            return LocalDate.now().withDayOfMonth(1);
        }
    }

    private org.springframework.http.HttpHeaders pdfHeaders(String filename) {
        org.springframework.http.HttpHeaders h = new org.springframework.http.HttpHeaders();
        h.set(org.springframework.http.HttpHeaders.CONTENT_DISPOSITION, "inline;filename=" + filename);
        return h;
    }

    /** Relatório SEAD — Liquidação de fatura com desconto AP04. Parâmetro: mes=yyyy-MM */
    @GetMapping("/relatorio/sead")
    public ResponseEntity<byte[]> relatorioSead(@RequestParam("mes") String mes)
            throws FileNotFoundException, JRException {
        byte[] pdf = reportService.gerarRelatorioSead(parseMes(mes), currentUserLogin());
        return ResponseEntity.ok()
                .headers(pdfHeaders("relatorio_sead_" + mes + ".pdf"))
                .contentType(org.springframework.http.MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    /** Relatório CGE — Compliance: transações GLOSADO/ALERTA com evidências e hashes. */
    @GetMapping("/relatorio/cge")
    public ResponseEntity<byte[]> relatorioCge(@RequestParam("mes") String mes)
            throws FileNotFoundException, JRException {
        byte[] pdf = reportService.gerarRelatorioCge(parseMes(mes), currentUserLogin());
        return ResponseEntity.ok()
                .headers(pdfHeaders("relatorio_cge_" + mes + ".pdf"))
                .contentType(org.springframework.http.MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    /** Relatório TCE — ROI mensal consolidado com certificado de integridade SHA-256. */
    @GetMapping("/relatorio/tce")
    public ResponseEntity<byte[]> relatorioTce(@RequestParam("mes") String mes)
            throws FileNotFoundException, JRException {
        byte[] pdf = reportService.gerarRelatorioTce(parseMes(mes), currentUserLogin());
        return ResponseEntity.ok()
                .headers(pdfHeaders("relatorio_tce_" + mes + ".pdf"))
                .contentType(org.springframework.http.MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}
