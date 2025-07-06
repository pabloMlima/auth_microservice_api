package com.hubsi.authmicroservice.dto.request;

import com.hubsi.authmicroservice.core.validation.email.UniqueEmail;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Requisição para registro de usuário")
public record RegisterRequest (
        @Schema(description = "Nome do usuário", example = "João", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "O nome é obrigatório.")
        @Size(min = 2, max = 50, message = "O nome deve ter entre 2 e 50 caracteres.")
        String nome,

        @Schema(description = "Sobrenome do usuário", example = "Silva")
        @NotBlank(message = "O sobrenome é obrigatório.")
        @Size(min = 2, max = 50, message = "O sobrenome deve ter entre 2 e 50 caracteres.")
        String sobrenome,

        @Schema(description = "Email do usuario", example = "usuario@email.com", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "O email é obrigatório.")
        @Email(message = "O email deve ser válido.")
        @UniqueEmail
        String email,

        @Schema(description = "Senha do usuário", example = "senhaSegura123", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "A senha é obrigatória.")
        @Size(min = 8, message = "A senha deve ter pelo menos 8 caracteres.")
        String password
) {
}
