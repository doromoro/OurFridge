package com.example.recipe2022.exception.auth;

public class OAuth2RegistrationException extends RuntimeException {
    public OAuth2RegistrationException(String message) {
        super(message);
    }
}