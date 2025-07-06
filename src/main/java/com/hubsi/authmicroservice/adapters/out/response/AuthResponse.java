package com.hubsi.authmicroservice.adapters.out.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "Resposta de autenticação do usuário")
public record AuthResponse(
        @Schema(description = "Token de autenticação do usuário", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJwYUBnbWFpbC5jb20iLCJpYXQiOjE3NTE1ODM4ODksImV4cCI6MTc1MTY3MDI4OX0.dvXSsjR16E7A8zboov625dqY4DTmtwWdw0CDAStLoiU")
        String token,

        @Schema(description = "UUID do refresh token", example = "fb502274-869d-46cb-805c-83e56571ab82")
        UUID refreshToken
) {
}
