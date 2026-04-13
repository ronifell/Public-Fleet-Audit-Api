package com.example.seagri.infra.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.seagri.infra.model.PatrimonyAsset;

public interface PatrimonyAssetRepository extends JpaRepository<PatrimonyAsset, Long> {

    Optional<PatrimonyAsset> findByTombo(String tombo);

    List<PatrimonyAsset> findBySituacao(String situacao);

    List<PatrimonyAsset> findByResponsavel(String responsavel);

    @Query("SELECT COUNT(a) FROM PatrimonyAsset a WHERE a.situacao = 'ATIVO'")
    long countAtivos();

    @Query("SELECT COUNT(a) FROM PatrimonyAsset a WHERE a.situacao = 'EM_REVISAO'")
    long countEmRevisao();

    @Query("SELECT COALESCE(SUM(a.valorPatrimonial), 0) FROM PatrimonyAsset a")
    java.math.BigDecimal sumValorPatrimonial();

    Optional<PatrimonyAsset> findTopByOrderByIdDesc();

    List<PatrimonyAsset> findAllByOrderByDataRegistroDesc();
}
