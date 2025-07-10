package com.hubsi.authmicroservice.infrastructure.exceptions;

public class JwtInvalidException extends RuntimeException {
    public JwtInvalidException(String message, Throwable cause) {
        super(message, cause);
    }
}
