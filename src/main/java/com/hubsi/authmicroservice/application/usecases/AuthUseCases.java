package com.hubsi.authmicroservice.application.usecases;

import com.hubsi.authmicroservice.adapters.out.persistence.entities.RefreshToken;
import com.hubsi.authmicroservice.adapters.out.persistence.entities.User;
import com.hubsi.authmicroservice.adapters.out.response.AuthResponse;

import java.time.Duration;
import java.util.UUID;

public interface AuthUseCases {

    AuthResponse authenticateUser(String email, String password);

    RefreshToken saveRefreshToken(Duration refreshTokenTtl, User user);

    AuthResponse refreshToken(UUID refreshToken);

    void revokeRefreshToken(UUID refreshToken);
}
