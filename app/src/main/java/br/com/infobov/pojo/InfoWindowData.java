package br.com.infobov.pojo;

public class InfoWindowData {

    private String fazenda;
    private String imagem;
    private String tipoImagem;

    public InfoWindowData() {
    }

    public InfoWindowData(String fazenda, String imagem, String tipoImagem) {
        this.fazenda = fazenda;
        this.imagem = imagem;
        this.tipoImagem = tipoImagem;
    }

    public String getFazenda() {
        return fazenda;
    }

    public void setFazenda(String fazenda) {
        this.fazenda = fazenda;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public String getTipoImagem() {
        return tipoImagem;
    }

    public void setTipoImagem(String tipoImagem) {
        this.tipoImagem = tipoImagem;
    }
}
