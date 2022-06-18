package com.utopiapp.demo.exceptions;

public class ActivityNameInUseException extends RuntimeException{
    private String message = "El nom d'aquesta activitat ja existeix";

    public ActivityNameInUseException() {
    }

    @Override
    public String getMessage() {
        return message;
    }
}
