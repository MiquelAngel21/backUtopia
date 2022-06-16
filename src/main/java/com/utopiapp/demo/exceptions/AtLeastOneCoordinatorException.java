package com.utopiapp.demo.exceptions;

public class AtLeastOneCoordinatorException extends RuntimeException{
    private String message = "Almanco ha de quedar un administrador en el club";

    public AtLeastOneCoordinatorException() {
    }

    @Override
    public String getMessage() {
        return message;
    }
}
