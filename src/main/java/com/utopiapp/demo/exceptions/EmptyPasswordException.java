package com.utopiapp.demo.exceptions;

public class EmptyPasswordException extends RuntimeException{
    private String message = "Aquest usuari no té contrasenya, primer hauria d'assignar una clicant el botó d'actualitza contrasenya en els settings i deixar en buit el camp de contrasenya actual";

    public EmptyPasswordException() {
    }

    @Override
    public String getMessage() {
        return message;
    }
}
