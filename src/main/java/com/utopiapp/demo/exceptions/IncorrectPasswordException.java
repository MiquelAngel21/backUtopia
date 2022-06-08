package com.utopiapp.demo.exceptions;

public class IncorrectPasswordException extends RuntimeException {
    private String message = "Error de coincidencia de contraseña";

    public IncorrectPasswordException() {
    }

    @Override
    public String getMessage() {
        return message;
    }
}
