package com.alessandra.api_sistema_de_chamados_ifrn.exception;

public class RequisicaoInvalidaException extends RuntimeException{
    public RequisicaoInvalidaException(String mensagem){
        super(mensagem);
    }
}
