package com.utopiapp.demo.exceptions;

public class TagNoExists extends RuntimeException{
    private String message = "S'ha afegit un tag no existent";

    public TagNoExists() {
    }

    @Override
    public String getMessage() {
        return message;
    }
}
