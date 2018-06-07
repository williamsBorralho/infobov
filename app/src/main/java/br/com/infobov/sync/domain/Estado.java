package br.com.infobov.sync.domain;

import java.io.Serializable;

public class Estado implements Serializable {

    private Integer id;
    private Integer codigo;
    private String uf;
    private String nome;
    private Integer regiao;

    public Estado() {
    }

    public Estado(String uf, String nome) {
        this.uf = uf;
        this.nome = nome;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getRegiao() {
        return regiao;
    }

    public void setRegiao(Integer regiao) {
        this.regiao = regiao;
    }

    public Estado(Integer id, Integer codigo, String uf, String nome, Integer regiao) {
        this.id = id;
        this.codigo = codigo;
        this.uf = uf;
        this.nome = nome;
        this.regiao = regiao;


    }
}
