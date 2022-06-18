package com.utopiapp.demo.exceptions;

public class ClubEmailInUseException extends RuntimeException{
    private String message = "Aquest email de club ja està en ús";

    public ClubEmailInUseException() {
    }

    @Override
    public String getMessage() {
        return message;
    }
}
