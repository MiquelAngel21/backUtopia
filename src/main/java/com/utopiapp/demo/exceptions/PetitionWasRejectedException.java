package com.utopiapp.demo.exceptions;

public class PetitionWasRejectedException extends RuntimeException{
    private String message = "Aquesta petició ja ha estat gestionada";

    public PetitionWasRejectedException() {
    }

    @Override
    public String getMessage() {
        return message;
    }
}
