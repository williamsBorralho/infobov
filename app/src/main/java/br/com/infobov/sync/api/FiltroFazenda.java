package br.com.infobov.sync.api;

import java.io.Serializable;

public class FiltroFazenda implements Serializable {

    private Integer tipoFiltro;
    private String palavraChave;

    public FiltroFazenda() {
    }

    public FiltroFazenda(Integer tipoFiltro, String palavraChave) {
        this.tipoFiltro = tipoFiltro;
        this.palavraChave = palavraChave;
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
}
