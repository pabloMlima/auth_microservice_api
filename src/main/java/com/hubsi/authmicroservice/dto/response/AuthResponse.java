package com.hubsi.authmicroservice.dto.response;

public record AuthResponse(
        String token,
        String message
) {
}
