package com.example.projetotestesjunit5.infrastructure.exception;

public class BusinessException extends RuntimeException{
    //Serialização do objeto, que serve para garantir que o objeto será salvo e recuperado de forma correta
    private static final long servialVersionUID = 1L;
    public BusinessException(String message){
        super(message);
    }
    public BusinessException(Throwable cause) {
        super(cause);
    }
    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}
