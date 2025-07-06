package com.hubsi.authmicroservice.adapters.in.controller;

import com.hubsi.authmicroservice.adapters.in.request.LoginRequest;
import com.hubsi.authmicroservice.adapters.out.response.AuthResponse;
import com.hubsi.authmicroservice.application.services.AuthService;
import com.hubsi.authmicroservice.application.services.UserService;
import com.hubsi.authmicroservice.application.usecases.UserUseCases;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Endpoints para autenticação de usuários")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Autenticar usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário autenticado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content),
            @ApiResponse(responseCode = "503", description = "Serviço indisponível", content = @Content),
            @ApiResponse(responseCode = "504", description = "Tempo limite de solicitação excedido", content = @Content),
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.authenticateUser(request.email(), request.password()));
    }

    @Operation(summary = "Atualizar token de autenticação")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token atualizado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
            @ApiResponse(responseCode = "401", description = "Token inválido ou expirado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content),
            @ApiResponse(responseCode = "503", description = "Serviço indisponível", content = @Content),
            @ApiResponse(responseCode = "504", description = "Tempo limite de solicitação excedido", content = @Content),
    })
    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponse> refreshToken(
            @RequestParam UUID refreshToken
    ) {
        return ResponseEntity.ok(authService.refreshToken(refreshToken));
    }

    @Operation(summary = "Revogar token de autenticação")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Token revogado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
            @ApiResponse(responseCode = "401", description = "Token inválido ou expirado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content),
            @ApiResponse(responseCode = "503", description = "Serviço indisponível", content = @Content),
            @ApiResponse(responseCode = "504", description = "Tempo limite de solicitação excedido", content = @Content),
    })
    @PostMapping("/logout")
    public ResponseEntity<Void> revokeToken(@RequestParam UUID refreshToken) {
        authService.revokeRefreshToken(refreshToken);
        return ResponseEntity.noContent().build();
    }
    /*
    @PostMapping("/reset-password")
    public ResponseEntity<Void> recoverPassword(@RequestParam String email) {
        userService.resetPassword(email);
        return ResponseEntity.noContent().build();
    }

     */

}