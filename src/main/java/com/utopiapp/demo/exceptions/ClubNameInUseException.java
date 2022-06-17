package com.utopiapp.demo.exceptions;

public class ClubNameInUseException extends RuntimeException{
    private String message = "Aquest nom de club ja està en ús";

    public ClubNameInUseException() {
    }

    @Override
    public String getMessage() {
        return message;
    }
}
