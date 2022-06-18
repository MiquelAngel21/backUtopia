package com.utopiapp.demo.exceptions;

public class UsernameAlreadyExistsException extends RuntimeException{
    private String message = "Aquest nom d'usuari, ja existeix";

    public UsernameAlreadyExistsException() {
    }

    @Override
    public String getMessage() {
        return message;
    }
}
