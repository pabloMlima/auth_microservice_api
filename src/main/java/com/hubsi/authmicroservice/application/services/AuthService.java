package com.hubsi.authmicroservice.application.services;

import com.hubsi.authmicroservice.adapters.out.response.AuthResponse;
import com.hubsi.authmicroservice.adapters.out.persistence.repository.JpaRefreshTokenRepository;
import com.hubsi.authmicroservice.adapters.out.persistence.repository.JpaUserRepository;
import com.hubsi.authmicroservice.adapters.out.security.UserDetailsImpl;
import com.hubsi.authmicroservice.application.usecases.AuthUseCases;
import com.hubsi.authmicroservice.domain.refresh_token.RefreshToken;
import com.hubsi.authmicroservice.domain.refresh_token.RefreshTokenRepository;
import com.hubsi.authmicroservice.domain.user.User;
import com.hubsi.authmicroservice.domain.user.UserRepository;
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
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;


    public AuthResponse authenticateUser(String email, String password) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        email,
                        password
                )
        );
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado!"));
        UserDetailsImpl userDetails = new UserDetailsImpl(user);

        String jwtToken = jwtService.generateToken(userDetails);
        final Duration refreshTokenTtl = Duration.ofDays(7);
        final RefreshToken refreshToken = saveRefreshToken(refreshTokenTtl, user);

        return new AuthResponse(jwtToken, refreshToken.getId());
    }

    public RefreshToken saveRefreshToken(Duration refreshTokenTtl, User user) {
        RefreshToken refreshToken = new RefreshToken(
                user,
                Instant.now(),
                Instant.now().plus(refreshTokenTtl)
        );
        return refreshTokenRepository.save(refreshToken);
    }

    public AuthResponse refreshToken(UUID refreshToken) {
        final var refreshTokenRes = refreshTokenRepository
                .findByIdAndExpiresAtAfter(refreshToken, Instant.now())
                .orElseThrow(() -> new UsernameNotFoundException("Refresh token inválido ou expirado!"));

        UserDetailsImpl userDetails = new UserDetailsImpl(refreshTokenRes.getUser());
        final var newAccessToken = jwtService.generateToken(userDetails);

        return new AuthResponse(newAccessToken, refreshTokenRes.getId());
    }

    public void revokeRefreshToken(UUID refreshToken) {
        jpaRefreshTokenRepository.deleteById(refreshToken);
    }
}
