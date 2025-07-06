package com.hubsi.authmicroservice.adapters.out.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.UUID;

@Builder
@Schema(description = "Resposta de registro de usuário")
public record RegisterResponse(
    @Schema(description = "Mensagem de sucesso", example = "Usuário registrado com sucesso.")
    String message,

    @Schema(description = "Token de autenticação do usuário", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJwYUBnbWFpbC5jb20iLCJpYXQiOjE3NTE1ODM4ODksImV4cCI6MTc1MTY3MDI4OX0.dvXSsjR16E7A8zboov625dqY4DTmtwWdw0CDAStLoiU")
    String token,

    @Schema(description = "Email do usuário", example = "usuario@email.com")
    String email,

    @Schema(description = "Nome do usuário", example = "João")
    String nome,

    @Schema(description = "Sobrenome do usuário", example = "Silva")
    String sobrenome,

    @Schema(description = "Função do usuário", example = "USER")
    String role,

    @Schema(description = "UUID do usuário", example = "fb502274-869d-46cb-805c-83e56571ab82")
    UUID id
) {
}
