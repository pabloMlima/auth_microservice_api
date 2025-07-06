package com.hubsi.authmicroservice.adapters.in.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Requisição para autenticação de usuário")
public record LoginRequest (

        @Schema(description = "Email do usuário", example = "usuario@email.com", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "O email é obrigatório.")
        @Email(message = "O email deve ser válido.")
        String email,

        @Schema(description = "Senha do usuário", example = "senhaSegura123", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "A senha é obrigatória.")
        @Size(min = 8, message = "A senha deve ter pelo menos 8 caracteres.")
        String password
) {
}
