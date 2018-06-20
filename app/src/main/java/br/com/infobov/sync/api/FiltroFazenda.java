package br.com.infobov.sync.api;

import java.io.Serializable;

public class FiltroFazenda implements Serializable {

    private Integer tipoFiltro;
    private String palavraChave;
    private String uf;
    private String municipio;

    public FiltroFazenda() {
    }

    public FiltroFazenda(Integer tipoFiltro, String palavraChave, String uf) {
        this.tipoFiltro = tipoFiltro;
        this.palavraChave = palavraChave;
        this.uf = uf;
    }

    public Integer getTipoFiltro() {
        return tipoFiltro;
    }

    public void setTipoFiltro(Integer tipoFiltro) {
        this.tipoFiltro = tipoFiltro;
    }

    public String getPalavraChave() {
        return palavraChave;
    }

    public void setPalavraChave(String palavraChave) {
        this.palavraChave = palavraChave;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }
}
