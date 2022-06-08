package com.utopiapp.demo.exceptions;

public class EmptyFieldsException extends RuntimeException{
    private String message = "Els camps obligatoris estan buits";

    public EmptyFieldsException() {
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
