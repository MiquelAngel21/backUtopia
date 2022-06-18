package com.utopiapp.demo.exceptions;

public class EmailAlreadyExistsException extends RuntimeException{
    private String message = "Aquest email, ja existeix";

    public EmailAlreadyExistsException() {
    }

    @Override
    public String getMessage() {
        return message;
    }
}
