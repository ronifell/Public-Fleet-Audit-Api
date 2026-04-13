package com.example.seagri.infra.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.seagri.infra.model.GlosaRecord;

public interface GlosaRepository extends JpaRepository<GlosaRecord, Long> {

    List<GlosaRecord> findByPlacaOrderByProcessedAtDesc(String placa);

    Optional<GlosaRecord> findTopByOrderByIdDesc();

    @Query("SELECT COUNT(g) FROM GlosaRecord g WHERE g.glosaStatus = ?1")
    long countByStatus(String status);

    @Query("SELECT COUNT(g) FROM GlosaRecord g WHERE g.glosaStatus != 'APROVADO'")
    long countAllGlosas();

    @Query("SELECT SUM(g.valorTotal) FROM GlosaRecord g WHERE g.glosaStatus = 'GLOSADO' AND g.valorTotal IS NOT NULL")
    BigDecimal sumValorGlosado();

    // ── Queries para os 3 relatórios governamentais ──────────────────────────

    List<GlosaRecord> findByTransacaoIdIn(List<String> transacaoIds);

    @Query("SELECT g FROM GlosaRecord g WHERE g.glosaStatus IN ?1 " +
           "AND g.transacaoTimestamp BETWEEN ?2 AND ?3 ORDER BY g.placa ASC, g.transacaoTimestamp ASC")
    List<GlosaRecord> findByStatusInAndPeriod(List<String> statuses,
                                              LocalDateTime inicio, LocalDateTime fim);

    @Query("SELECT g FROM GlosaRecord g WHERE g.transacaoTimestamp BETWEEN ?1 AND ?2 " +
           "ORDER BY g.placa ASC, g.transacaoTimestamp ASC")
    List<GlosaRecord> findByPeriod(LocalDateTime inicio, LocalDateTime fim);

    @Query("SELECT COUNT(DISTINCT g.placa) FROM GlosaRecord g WHERE g.transacaoTimestamp BETWEEN ?1 AND ?2")
    long countDistinctPlacaByPeriod(LocalDateTime inicio, LocalDateTime fim);

    @Query("SELECT SUM(g.valorTotal) FROM GlosaRecord g " +
           "WHERE g.glosaStatus = 'GLOSADO' AND g.valorTotal IS NOT NULL " +
           "AND g.transacaoTimestamp BETWEEN ?1 AND ?2")
    BigDecimal sumValorGlosadoByPeriod(LocalDateTime inicio, LocalDateTime fim);

    @Query("SELECT SUM(g.valorTotal) FROM GlosaRecord g " +
           "WHERE g.valorTotal IS NOT NULL AND g.transacaoTimestamp BETWEEN ?1 AND ?2")
    BigDecimal sumValorBrutoByPeriod(LocalDateTime inicio, LocalDateTime fim);

    @Query("SELECT g.integrityHash FROM GlosaRecord g " +
           "WHERE g.transacaoTimestamp BETWEEN ?1 AND ?2 ORDER BY g.id ASC")
    List<String> findAllHashesByPeriod(LocalDateTime inicio, LocalDateTime fim);
}
