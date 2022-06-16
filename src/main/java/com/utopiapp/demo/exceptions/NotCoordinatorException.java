package com.utopiapp.demo.exceptions;

public class NotCoordinatorException extends RuntimeException{
    private String message = "No ets un administrador, per favor no accedeixis aquí si no tens aquest rol";

    public NotCoordinatorException() {
    }

    @Override
    public String getMessage() {
        return message;
    }
}
