package br.com.infobov.sync.domain;

import java.io.Serializable;
import java.math.BigDecimal;

public class Fazenda implements Serializable {

    private Integer id;
    private String nome;
    private String apelido;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private Boolean geoDivergencia;
    private String caracteresDaMarca;
    private String dicasDaMarca;
    private String observacao;
    private Proprietario proprietario;
    private String ferro;


    public Fazenda() {
    }

    public Fazenda(Integer id, String nome, String apelido, BigDecimal latitude, BigDecimal longitude, Boolean geoDivergencia, String caracteresDaMarca, String dicasDaMarca, String observacao) {
        this.id = id;
        this.nome = nome;
        this.apelido = apelido;
        this.latitude = latitude;
        this.longitude = longitude;
        this.geoDivergencia = geoDivergencia;
        this.caracteresDaMarca = caracteresDaMarca;
        this.dicasDaMarca = dicasDaMarca;
        this.observacao = observacao;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getApelido() {
        return apelido;
    }

    public void setApelido(String apelido) {
        this.apelido = apelido;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public Boolean getGeoDivergencia() {
        return geoDivergencia;
    }

    public void setGeoDivergencia(Boolean geoDivergencia) {
        this.geoDivergencia = geoDivergencia;
    }

    public String getCaracteresDaMarca() {
        return caracteresDaMarca;
    }

    public void setCaracteresDaMarca(String caracteresDaMarca) {
        this.caracteresDaMarca = caracteresDaMarca;
    }

    public String getDicasDaMarca() {
        return dicasDaMarca;
    }

    public void setDicasDaMarca(String dicasDaMarca) {
        this.dicasDaMarca = dicasDaMarca;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }


    public Proprietario getProprietario() {
        return proprietario;
    }

    public void setProprietario(Proprietario proprietario) {
        this.proprietario = proprietario;
    }

    public String getFerro() {
        return ferro;
    }

    public void setFerro(String ferro) {
        this.ferro = ferro;
    }
}
