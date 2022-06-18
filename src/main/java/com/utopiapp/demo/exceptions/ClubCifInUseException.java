package com.utopiapp.demo.exceptions;

public class ClubCifInUseException extends RuntimeException{
    private String message = "El Cif d'aquest club és repetit";

    public ClubCifInUseException() {
    }

    @Override
    public String getMessage() {
        return message;
    }
}
