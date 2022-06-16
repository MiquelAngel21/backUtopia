package com.utopiapp.demo.exceptions;

public class PetitionTimeNotExpired extends RuntimeException{
    private String message = "Ja has creat una petició per aquest club, torna a fer un altre el dia següent";

    public PetitionTimeNotExpired() {
    }

    @Override
    public String getMessage() {
        return message;
    }
}
