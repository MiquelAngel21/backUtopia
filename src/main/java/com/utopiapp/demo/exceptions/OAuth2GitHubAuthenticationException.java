package com.utopiapp.demo.exceptions;

public class OAuth2GitHubAuthenticationException extends RuntimeException{
    private String message = "L'autenticaci√≥ amb Github ha fallat";

    public OAuth2GitHubAuthenticationException() {
    }

    @Override
    public String getMessage() {
        return message;
    }
}
