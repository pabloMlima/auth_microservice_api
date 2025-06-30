package com.hubsi.authmicroservice.dto.request;

import com.hubsi.authmicroservice.core.validation.email.UniqueEmail;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest (
        @NotBlank(message = "O nome é obrigatório.")
        @Size(min = 2, max = 50, message = "O nome deve ter entre 2 e 50 caracteres.")
        String nome,

        @NotBlank(message = "O sobrenome é obrigatório.")
        @Size(min = 2, max = 50, message = "O sobrenome deve ter entre 2 e 50 caracteres.")
        String sobrenome,

        @NotBlank(message = "O email é obrigatório.")
        @Email(message = "O email deve ser válido.")
        @UniqueEmail
        String email,

        @NotBlank(message = "A senha é obrigatória.")
        @Size(min = 8, message = "A senha deve ter pelo menos 8 caracteres.")
        String password
) {
}
