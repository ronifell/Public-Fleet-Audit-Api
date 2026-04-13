package com.example.seagri.infra.model;



import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "CHECKLISTS")
@Getter
@Setter 
@NoArgsConstructor
@AllArgsConstructor
public class Checklist extends BaseEntity{

    @Id
    @Column(name = "travel_id")
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @MapsId
    @JoinColumn(name = "travel_id")
    @JsonIgnore
    private VehicleTravel travel;

    @Column
    private String buzina;

    @Column
    private String cinto;

    @Column
    private String vidros;

    @Column
    private String macaco;

    @Column
    private String quebraSol;

    @Column
    private String triangulo;

    @Column
    private String retrovisorInterno;

    @Column
    private String retrovisoresLaterais;

    @Column
    private String chaveDeRoda;

    @Column
    private String extensor;

    @Column
    private String indicadoresPainel;

    @Column
    private String luzPlaca;

    @Column
    private String oleoMotor;

    @Column
    private String oleoFreio;

    @Column
    private String luzFreio;

    @Column
    private String luzRe;

    @Column
    private String nivelAgua;

    @Column
    private String alarme;

    @Column
    private String pneus;

    @Column
    private String travas;

    @Column
    private String farois;

    @Column
    private String extintor;

    @Column
    private String lanternasDianteiras;

    @Column
    private String lanternasTraseiras;

    @Column
    private String estepe;

    @Column
    private String alerta;

    @Column
    private String cartaoAbastecimentoComCondutor;

    @Column
    private String habilitacaoCondutor;

    @Column
    private String vencimentoCarteira;

    @Column
    private String categoriaCarteira;

    @Column
    private String paraBrisas;

    @Column
    private String bancos;

    @Column
    private String documentoVeiculo;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] assinaturaResponsavel;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] assinaturaMotorista;
    
}
