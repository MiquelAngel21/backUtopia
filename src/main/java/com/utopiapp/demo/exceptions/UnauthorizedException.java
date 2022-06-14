package com.utopiapp.demo.exceptions;

public class UnauthorizedException extends RuntimeException{
    private String message = "No tens permís per realitzar aquesta acció";

    public UnauthorizedException() {
    }

    @Override
    public String getMessage() {
        return message;
    }
}
