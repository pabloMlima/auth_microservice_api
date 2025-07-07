package com.hubsi.authmicroservice.application.usecases;

import com.hubsi.authmicroservice.adapters.out.persistence.entities.RefreshTokenEntity;
import com.hubsi.authmicroservice.adapters.out.persistence.entities.UserEntity;
import com.hubsi.authmicroservice.adapters.out.response.AuthResponse;

import java.time.Duration;
import java.util.UUID;

public interface AuthUseCases {

    AuthResponse authenticateUser(String email, String password);

    RefreshTokenEntity saveRefreshToken(Duration refreshTokenTtl, UserEntity userEntity);

    AuthResponse refreshToken(UUID refreshToken);

    void revokeRefreshToken(UUID refreshToken);
}
