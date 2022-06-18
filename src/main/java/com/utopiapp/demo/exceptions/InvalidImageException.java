package com.utopiapp.demo.exceptions;

public class InvalidImageException extends RuntimeException{
    private String message = "El fitxer, només pot ser una imatge";

    public InvalidImageException() {
    }

    @Override
    public String getMessage() {
        return message;
    }
}
