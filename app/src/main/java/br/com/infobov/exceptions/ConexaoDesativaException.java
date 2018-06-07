package br.com.infobov.exceptions;

/**
 * Created by will_ on 08/03/2016.
 */
public class ConexaoDesativaException extends Exception {

    public ConexaoDesativaException(String detailMessage) {
        super(detailMessage);
    }

    public ConexaoDesativaException() {
        super("Desculpe, mas parece que está desativada sua conexão com a rede!!");
    }


}
