package com.example.seagri.infra.integrity;

/**
 * Entidades que implementam esta interface fornecem os dados
 * que serão combinados para gerar o hash SHA-256 de integridade.
 */
public interface Hashable {
    /**
     * Retorna uma string canônica dos campos imutáveis do registro.
     * Essa string é o que entra no SHA-256 — deve ser determinística e
     * incluir apenas campos que não mudam após a criação.
     */
    String toHashString();
}
