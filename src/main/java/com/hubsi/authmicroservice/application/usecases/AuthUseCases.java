package com.hubsi.authmicroservice.application.usecases;

import com.hubsi.authmicroservice.adapters.out.persistence.entities.RefreshTokenEntity;
import com.hubsi.authmicroservice.adapters.out.persistence.entities.UserEntity;
import com.hubsi.authmicroservice.adapters.out.response.AuthResponse;
import com.hubsi.authmicroservice.domain.refresh_token.RefreshToken;
import com.hubsi.authmicroservice.domain.user.User;

import java.time.Duration;
import java.util.UUID;

public interface AuthUseCases {

    AuthResponse authenticateUser(String email, String password);

    RefreshToken saveRefreshToken(Duration refreshTokenTtl, User user);

    AuthResponse refreshToken(UUID refreshToken);

    void revokeRefreshToken(UUID refreshToken);
}
