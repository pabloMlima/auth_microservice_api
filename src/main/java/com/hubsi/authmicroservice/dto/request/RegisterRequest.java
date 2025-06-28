package com.hubsi.authmicroservice.dto.request;

public record RegisterRequest (
        String nome,
        String sobrenome,
        String email,
        String password
) {
}
