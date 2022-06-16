package com.utopiapp.demo.exceptions;

public class CodeNotExistsException extends RuntimeException{
    private String message = "Aquest codi d'accés no existeix";

    public CodeNotExistsException() {
    }

    @Override
    public String getMessage() {
        return message;
    }
}
