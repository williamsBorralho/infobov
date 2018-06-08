package br.com.infobov.sync.domain;

import java.io.Serializable;

public class Municipio implements Serializable {

    private Integer codigo;
    private String nome;
    private String uf;
    private Integer regiao;

    public Municipio(String nome, String uf) {
        this.nome = nome;
        this.uf = uf;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public Integer getRegiao() {
        return regiao;
    }

    public void setRegiao(Integer regiao) {
        this.regiao = regiao;
    }
}
