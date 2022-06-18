package com.utopiapp.demo.exceptions;

public class NegativeAmountException extends RuntimeException{
    private String message = "La quantitat de material no pot ser negativa";

    public NegativeAmountException() {
    }

    @Override
    public String getMessage() {
        return message;
    }
}
