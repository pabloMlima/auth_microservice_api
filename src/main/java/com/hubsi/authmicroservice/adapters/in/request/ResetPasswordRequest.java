package com.hubsi.authmicroservice.adapters.in.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ResetPasswordRequest(
        @NotBlank(message = "O email é obrigatório")
        @Email(message = "Formato de email inválido")
        String email
) {
}
