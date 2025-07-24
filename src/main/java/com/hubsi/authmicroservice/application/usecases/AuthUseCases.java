package com.hubsi.authmicroservice.application.usecases;

import com.hubsi.authmicroservice.adapters.out.response.AuthResponse;
import com.hubsi.authmicroservice.domain.refresh_token.RefreshToken;
import com.hubsi.authmicroservice.domain.user.User;

import java.time.Duration;
import java.util.UUID;

public interface AuthUseCases {

    /**
     * Autentica um usuário com o e-mail e senha fornecidos.
     * @param email o e-mail do usuário
     * @param password a senha do usuário
     * @return um AuthResponse contendo o token JWT e o ID do refresh token
     */
    AuthResponse authenticateUser(String email, String password);

    /**
     * Salva um refresh token para o usuário com o tempo de vida especificado.
     * @param refreshTokenTtl a duração do tempo de vida do refresh token
     * @param user o usuário associado ao refresh token
     * @return o RefreshToken salvo
     */
    RefreshToken saveRefreshToken(Duration refreshTokenTtl, User user);

    /**
     * Atualiza o refresh token usando o ID do refresh token fornecido.
     * @param refreshToken o ID do refresh token
     * @return um AuthResponse contendo o novo token JWT e o ID do refresh token atualizado
     */
    AuthResponse refreshToken(UUID refreshToken);

    /**
     * Revoga o refresh token especificado.
     * @param refreshToken o ID do refresh token a ser revogado
     */
    void revokeRefreshToken(UUID refreshToken);
}
