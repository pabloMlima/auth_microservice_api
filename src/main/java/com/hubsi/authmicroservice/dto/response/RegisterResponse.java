package com.hubsi.authmicroservice.dto.response;

import lombok.Builder;

import java.util.UUID;

@Builder
public record RegisterResponse(
    String message,
    String token,
    String email,
    String nome,
    String sobrenome,
    String role,
    UUID id
) {
}
