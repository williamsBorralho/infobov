package br.com.infobov.sync.domain;

import java.io.Serializable;

public class Notifcacao implements Serializable {
    private String owner;
    private String data;
    private Integer quantidade;
    private String sexo;
    private String observacao;
    private String tipo;
    private Fazenda fazenda;
    private Proprietario proprietario;

    public Notifcacao() {
    }

    public Notifcacao(String owner, String data, Integer quantidade, String sexo, String observacao, String tipo, Fazenda fazenda,
                           Proprietario proprietario) {
        super();
        this.owner = owner;
        this.data = data;
        this.quantidade = quantidade;
        this.sexo = sexo;
        this.observacao = observacao;
        this.tipo = tipo;
        this.fazenda = fazenda;
        this.proprietario = proprietario;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Fazenda getFazenda() {
        return fazenda;
    }

    public void setFazenda(Fazenda fazenda) {
        this.fazenda = fazenda;
    }

    public Proprietario getProprietario() {
        return proprietario;
    }

    public void setProprietario(Proprietario proprietario) {
        this.proprietario = proprietario;
    }
}
