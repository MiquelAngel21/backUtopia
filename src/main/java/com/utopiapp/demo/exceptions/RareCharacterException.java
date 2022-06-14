package com.utopiapp.demo.exceptions;

public class RareCharacterException extends RuntimeException{
    private String message = "Només es poden utilitzar els caràcters alfanumèrics";

    public RareCharacterException() {
    }

    @Override
    public String getMessage() {
        return message;
    }
}
