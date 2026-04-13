package com.example.seagri.infra.dto;

/**
 * Linha do Relatório CGE — Compliance e Transparência.
 * Cada linha representa uma transação GLOSADA ou em ALERTA,
 * com evidência geográfica e hash SHA-256 para fé pública.
 */
public class GlosaCgeRowDTO {

    private String placa;
    private String evidencia;
    private String motivoGlosa;
    private String hashSha256;

    public GlosaCgeRowDTO() {}

    public GlosaCgeRowDTO(String placa, String evidencia, String motivoGlosa, String hashSha256) {
        this.placa = placa;
        this.evidencia = evidencia;
        this.motivoGlosa = motivoGlosa;
        this.hashSha256 = hashSha256;
    }

    public String getPlaca()       { return placa; }
    public String getEvidencia()   { return evidencia; }
    public String getMotivoGlosa() { return motivoGlosa; }
    public String getHashSha256()  { return hashSha256; }

    public void setPlaca(String placa)             { this.placa = placa; }
    public void setEvidencia(String evidencia)     { this.evidencia = evidencia; }
    public void setMotivoGlosa(String motivoGlosa) { this.motivoGlosa = motivoGlosa; }
    public void setHashSha256(String hashSha256)   { this.hashSha256 = hashSha256; }
}
