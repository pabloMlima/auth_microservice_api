package com.hubsi.authmicroservice.adapters.in.controller;

import com.hubsi.authmicroservice.adapters.in.request.RegisterRequest;
import com.hubsi.authmicroservice.adapters.in.request.ResetPasswordRequest;
import com.hubsi.authmicroservice.adapters.out.response.RegisterResponse;
import com.hubsi.authmicroservice.application.usecases.ResetPasswordUseCases;
import com.hubsi.authmicroservice.application.usecases.UserUseCases;
import com.hubsi.authmicroservice.infrastructure.constants.RoutesConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@Tag(name = "User", description = "Endpoints para gerenciamento de usuários")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserUseCases userUseCases;

    private final ResetPasswordUseCases resetPasswordUseCases;

    @Operation(summary = "Registrar um novo usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário registrado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = RegisterResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
            @ApiResponse(responseCode = "409", description = "Usuário já existe", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content),
            @ApiResponse(responseCode = "503", description = "Serviço indisponível", content = @Content),
            @ApiResponse(responseCode = "504", description = "Tempo limite de solicitação excedido", content = @Content),
    })
    @PostMapping(value="/register", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(userUseCases.registerUser(request));
    }

    @Operation(summary = "Redefinir senha do usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Senha redefinida com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content),
            @ApiResponse(responseCode = "503", description = "Serviço indisponível", content = @Content),
            @ApiResponse(responseCode = "504", description = "Tempo limite de solicitação excedido", content = @Content),
    })
    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        resetPasswordUseCases.resetPassword(request.email());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Confirmar redefinição de senha")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Senha atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
            @ApiResponse(responseCode = "404", description = "Token inválido ou expirado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content),
            @ApiResponse(responseCode = "503", description = "Serviço indisponível", content = @Content),
            @ApiResponse(responseCode = "504", description = "Tempo limite de solicitação excedido", content = @Content),
    })
    @PutMapping(RoutesConstants.USER_UPDATE_PASSWORD)
    public ResponseEntity<Void> updatePassword(@RequestParam String token, @RequestParam String newPassword) {
        resetPasswordUseCases.confirmResetPassword(token, newPassword);
        return ResponseEntity.noContent().build();
    }

}
