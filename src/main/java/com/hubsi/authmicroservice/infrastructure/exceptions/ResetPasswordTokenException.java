package com.hubsi.authmicroservice.infrastructure.exceptions;

public class ResetPasswordTokenException extends RuntimeException {
    public ResetPasswordTokenException(String message) {
        super(message);
    }
}
