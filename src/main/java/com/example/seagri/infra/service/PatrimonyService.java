package com.example.seagri.infra.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.seagri.infra.dto.PatrimonyAssetInputDTO;
import com.example.seagri.infra.dto.PatrimonyTransferInputDTO;
import com.example.seagri.infra.integrity.HashService;
import com.example.seagri.infra.model.PatrimonyAsset;
import com.example.seagri.infra.model.PatrimonyTransfer;
import com.example.seagri.infra.repository.PatrimonyAssetRepository;
import com.example.seagri.infra.repository.PatrimonyTransferRepository;

/**
 * Serviço do módulo de Patrimônio.
 *
 * Responsabilidades:
 *   - CRUD de bens patrimoniais com hash SHA-256 encadeado
 *   - Registro de transferências com cadeia de custódia (quem enviou, quem recebeu, GPS)
 *   - Bloqueio de baixa sem documento/foto anexado (Item 7)
 *   - Reconciliação de inventário teórico vs real (Item 8)
 */
@Service
public class PatrimonyService {

    @Autowired private PatrimonyAssetRepository assetRepository;
    @Autowired private PatrimonyTransferRepository transferRepository;
    @Autowired private HashService hashService;

    // ── Assets ────────────────────────────────────────────────────────────────

    public PatrimonyAsset saveAsset(PatrimonyAssetInputDTO input) {
        PatrimonyAsset asset = new PatrimonyAsset();
        asset.setTombo(input.tombo());
        asset.setDescricao(input.descricao());
        asset.setCategoria(input.categoria());
        asset.setLat(input.lat());
        asset.setLng(input.lng());
        asset.setConservacaoPercent(input.conservacaoPercent());
        asset.setResponsavel(input.responsavel());
        asset.setSituacao(input.situacao() != null ? input.situacao() : "ATIVO");
        asset.setValorPatrimonial(input.valorPatrimonial());
        asset.setDocumentoAnexo(input.documentoAnexo());
        asset.setDataRegistro(LocalDateTime.now());

        String previousHash = assetRepository.findTopByOrderByIdDesc()
                .map(PatrimonyAsset::getIntegrityHash)
                .orElse("GENESIS");
        asset.setIntegrityHash(hashService.generate(previousHash + "|" + asset.toHashString()));

        return assetRepository.save(asset);
    }

    /**
     * Item 7 — Atualiza a situação de um bem.
     * Se a nova situação for "BAIXA", exige documento/foto anexado; caso contrário rejeita.
     */
    public PatrimonyAsset updateSituacao(String tombo, String novaSituacao, String documentoAnexo) {
        PatrimonyAsset asset = assetRepository.findByTombo(tombo)
                .orElseThrow(() -> new IllegalArgumentException("Bem não encontrado: " + tombo));

        if ("BAIXA".equalsIgnoreCase(novaSituacao)) {
            if (documentoAnexo == null || documentoAnexo.isBlank()) {
                throw new IllegalStateException(
                        "Não é possível dar baixa no bem " + tombo
                        + " sem documento ou foto anexada justificando a operação.");
            }
            asset.setDocumentoAnexo(documentoAnexo);
        }

        asset.setSituacao(novaSituacao.toUpperCase());
        return assetRepository.save(asset);
    }

    public List<PatrimonyAsset> listAll() {
        return assetRepository.findAllByOrderByDataRegistroDesc();
    }

    public Optional<PatrimonyAsset> findByTombo(String tombo) {
        return assetRepository.findByTombo(tombo);
    }

    public Map<String, Object> dashboard() {
        Map<String, Object> summary = new HashMap<>();
        summary.put("total_bens_catalogados", assetRepository.count());
        summary.put("bens_ativos", assetRepository.countAtivos());
        summary.put("bens_em_revisao", assetRepository.countEmRevisao());
        summary.put("valor_patrimonial_total", assetRepository.sumValorPatrimonial());
        summary.put("total_transferencias", transferRepository.count());
        return summary;
    }

    // ── Transfers (Item 6 — full custody chain) ──────────────────────────────

    /**
     * Registra uma transferência de bem patrimonial, guardando remetente, destinatário,
     * GPS de ambos, motivo e hash encadeado.
     * Atualiza automaticamente o responsável e a situação do ativo para "TRANSFERIDO".
     */
    public PatrimonyTransfer saveTransfer(PatrimonyTransferInputDTO input) {
        PatrimonyAsset asset = assetRepository.findByTombo(input.tombo())
                .orElseThrow(() -> new IllegalArgumentException("Bem não encontrado: " + input.tombo()));

        PatrimonyTransfer transfer = new PatrimonyTransfer(
                input.tombo(),
                input.remetenteNome(), input.remetenteUnidade(),
                input.remetenteLat(), input.remetenteLng(),
                input.destinatarioNome(), input.destinatarioUnidade(),
                input.destinatarioLat(), input.destinatarioLng(),
                input.motivo(), LocalDateTime.now(), null);

        String previousHash = transferRepository.findTopByOrderByIdDesc()
                .map(PatrimonyTransfer::getIntegrityHash)
                .orElse("GENESIS");
        transfer.setIntegrityHash(hashService.generate(previousHash + "|" + transfer.toHashString()));

        PatrimonyTransfer saved = transferRepository.save(transfer);

        asset.setSituacao("TRANSFERIDO");
        asset.setResponsavel(input.destinatarioNome());
        assetRepository.save(asset);

        return saved;
    }

    public List<PatrimonyTransfer> listTransfers() {
        return transferRepository.findAllByOrderByDataTransferenciaDesc();
    }

    public List<PatrimonyTransfer> listTransfersByTombo(String tombo) {
        return transferRepository.findByTomboOrderByDataTransferenciaDesc(tombo);
    }

    // ── Item 8 — Inventory Reconciliation ────────────────────────────────────

    /**
     * Compara o inventário teórico (todos os bens cadastrados como ATIVO) com uma lista
     * de tombos efetivamente encontrados durante uma vistoria (inventário real).
     *
     * Retorna um mapa com:
     *   - encontrados: tombos que estão no sistema E foram vistos na vistoria
     *   - nao_encontrados: tombos que estão no sistema mas NÃO foram vistos
     *   - excedentes: tombos vistos na vistoria que NÃO constam no sistema
     *   - resumo: totais numéricos
     */
    public Map<String, Object> reconciliarInventario(List<String> tombosVistoriados) {
        List<PatrimonyAsset> teorico = assetRepository.findBySituacao("ATIVO");
        Map<String, PatrimonyAsset> mapaTeoricoByTombo = new HashMap<>();
        for (PatrimonyAsset a : teorico) {
            mapaTeoricoByTombo.put(a.getTombo(), a);
        }

        List<Map<String, Object>> encontrados = new ArrayList<>();
        List<Map<String, Object>> naoEncontrados = new ArrayList<>();
        List<String> excedentes = new ArrayList<>();

        java.util.Set<String> tombosVistoriadosSet = new java.util.HashSet<>(tombosVistoriados);

        for (PatrimonyAsset asset : teorico) {
            Map<String, Object> row = new HashMap<>();
            row.put("tombo", asset.getTombo());
            row.put("descricao", asset.getDescricao());
            row.put("responsavel", asset.getResponsavel());
            row.put("valor_patrimonial", asset.getValorPatrimonial());

            if (tombosVistoriadosSet.contains(asset.getTombo())) {
                row.put("status", "ENCONTRADO");
                encontrados.add(row);
            } else {
                row.put("status", "NAO_ENCONTRADO");
                naoEncontrados.add(row);
            }
        }

        for (String tombo : tombosVistoriados) {
            if (!mapaTeoricoByTombo.containsKey(tombo)) {
                excedentes.add(tombo);
            }
        }

        BigDecimal valorNaoEncontrado = naoEncontrados.stream()
                .map(r -> (BigDecimal) r.get("valor_patrimonial"))
                .filter(v -> v != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, Object> resumo = new HashMap<>();
        resumo.put("total_teorico", teorico.size());
        resumo.put("total_vistoriado", tombosVistoriados.size());
        resumo.put("encontrados", encontrados.size());
        resumo.put("nao_encontrados", naoEncontrados.size());
        resumo.put("excedentes", excedentes.size());
        resumo.put("valor_nao_encontrado", valorNaoEncontrado);

        Map<String, Object> result = new HashMap<>();
        result.put("resumo", resumo);
        result.put("encontrados", encontrados);
        result.put("nao_encontrados", naoEncontrados);
        result.put("excedentes", excedentes);
        return result;
    }
}
