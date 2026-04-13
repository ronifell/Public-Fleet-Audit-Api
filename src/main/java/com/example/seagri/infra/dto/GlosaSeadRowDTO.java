package com.example.seagri.infra.dto;

/**
 * Linha do Relatório SEAD — Liquidação de Fatura.
 * Cada linha representa um abastecimento com os valores bruto, glosado e líquido.
 */
public class GlosaSeadRowDTO {

    private String placa;
    private String dataTransacao;
    private String valorBruto;
    private String valorGlosado;
    private String valorLiquido;

    public GlosaSeadRowDTO() {}

    public GlosaSeadRowDTO(String placa, String dataTransacao,
                           String valorBruto, String valorGlosado, String valorLiquido) {
        this.placa = placa;
        this.dataTransacao = dataTransacao;
        this.valorBruto = valorBruto;
        this.valorGlosado = valorGlosado;
        this.valorLiquido = valorLiquido;
    }

    public String getPlaca()          { return placa; }
    public String getDataTransacao()  { return dataTransacao; }
    public String getValorBruto()     { return valorBruto; }
    public String getValorGlosado()   { return valorGlosado; }
    public String getValorLiquido()   { return valorLiquido; }

    public void setPlaca(String placa)                   { this.placa = placa; }
    public void setDataTransacao(String dataTransacao)   { this.dataTransacao = dataTransacao; }
    public void setValorBruto(String valorBruto)         { this.valorBruto = valorBruto; }
    public void setValorGlosado(String valorGlosado)     { this.valorGlosado = valorGlosado; }
    public void setValorLiquido(String valorLiquido)     { this.valorLiquido = valorLiquido; }
}
