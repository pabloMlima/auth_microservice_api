package com.hubsi.authmicroservice.dto.request;

public record LoginRequest (
        String email,
        String password
) {
}
