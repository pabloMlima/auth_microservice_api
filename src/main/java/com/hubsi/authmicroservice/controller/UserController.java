package com.hubsi.authmicroservice.controller;

import com.hubsi.authmicroservice.dto.request.RegisterRequest;
import com.hubsi.authmicroservice.dto.response.RegisterResponse;
import com.hubsi.authmicroservice.entity.User;
import com.hubsi.authmicroservice.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "User", description = "Endpoints para gerenciamento de usuários")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

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
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(userService.registerUser(request));
    }

}
