package br.com.infobov.sync.api;

public enum TipoFiltro {

    FAZENDA(1, "Propriedade Rural", " o nome da propriedade"),
    PROPRIETARIO(2, "Proprietário/Gestor", " o nome do proprietário"),
    CARACTERES(3, "Caracteres - Marca", " os caracteres da marca (letras e/ou números)"),
    DICA(4, "Dica - Marca", " uma dica da grafia da marca");

    private Integer codigo;
    private String descricao;
    private String mensagemFiltro;

    private TipoFiltro(Integer codigo, String descricao, String mensagemFiltro) {
        this.codigo = codigo;
        this.descricao = descricao;
        this.mensagemFiltro = mensagemFiltro;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getMensagemFiltro() {
        return mensagemFiltro;
    }

    public void setMensagemFiltro(String mensagemFiltro) {
        this.mensagemFiltro = mensagemFiltro;
    }

    //#

    public static TipoFiltro[] deNotificacoes() {
        TipoFiltro[] tipos = {TipoFiltro.PROPRIETARIO, TipoFiltro.FAZENDA};
        return tipos;
    }

    public static TipoFiltro byName(String name) {
        for (TipoFiltro tipo : values()) {
            if (tipo.name().equals(name)) {
                return tipo;
            }
        }
        return null;
    }

    public static TipoFiltro byDescricao(String descricao) {
        for (TipoFiltro tipo : values()) {
            if (tipo.getDescricao().toUpperCase().equals(descricao.toUpperCase())) {
                return tipo;
            }
        }
        return null;
    }
}
