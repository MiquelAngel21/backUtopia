package com.utopiapp.demo.exceptions;

public class AlreadyInAClubException extends RuntimeException{
    private String message = "Aquest usuari, ja està dins un altre club d'esplai";

    public AlreadyInAClubException() {
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
