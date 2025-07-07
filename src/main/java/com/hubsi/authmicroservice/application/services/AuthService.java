package com.hubsi.authmicroservice.application.services;

import com.hubsi.authmicroservice.adapters.out.response.AuthResponse;
import com.hubsi.authmicroservice.adapters.out.persistence.entities.RefreshTokenEntity;
import com.hubsi.authmicroservice.adapters.out.persistence.entities.UserEntity;
import com.hubsi.authmicroservice.adapters.out.persistence.repository.JpaRefreshTokenRepository;
import com.hubsi.authmicroservice.adapters.out.persistence.repository.JpaUserRepository;
import com.hubsi.authmicroservice.application.usecases.AuthUseCases;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class AuthService implements AuthUseCases {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final JpaUserRepository jpaUserRepository;
    private final JpaRefreshTokenRepository jpaRefreshTokenRepository;


    public AuthResponse authenticateUser(String email, String password) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        email,
                        password
                )
        );
        UserEntity userEntity = jpaUserRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado!"));

        String jwtToken = jwtService.generateToken(userEntity);
        final Duration refreshTokenTtl = Duration.ofDays(7);
        final RefreshTokenEntity refreshTokenEntity = saveRefreshToken(refreshTokenTtl, userEntity);

        return new AuthResponse(jwtToken, refreshTokenEntity.getId());
    }

    public RefreshTokenEntity saveRefreshToken(Duration refreshTokenTtl, UserEntity userEntity) {
        RefreshTokenEntity refreshTokenEntity = new RefreshTokenEntity();
        refreshTokenEntity.setUserEntity(userEntity);
        refreshTokenEntity.setExpiresAt(Instant.now().plus(refreshTokenTtl));
        return jpaRefreshTokenRepository.save(refreshTokenEntity);
    }

    public AuthResponse refreshToken(UUID refreshToken) {
        final var refreshTokenEntity = jpaRefreshTokenRepository
                .findByIdAndExpiresAtAfter(refreshToken, Instant.now())
                .orElseThrow(() -> new UsernameNotFoundException("Refresh token inválido ou expirado!"));

        final var newAccessToken = jwtService.generateToken(refreshTokenEntity.getUserEntity());

        return new AuthResponse(newAccessToken, refreshToken);
    }

    public void revokeRefreshToken(UUID refreshToken) {
        jpaRefreshTokenRepository.deleteById(refreshToken);
    }
}
