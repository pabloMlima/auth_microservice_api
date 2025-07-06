package com.hubsi.authmicroservice.application.services;

import com.hubsi.authmicroservice.adapters.out.response.AuthResponse;
import com.hubsi.authmicroservice.adapters.out.persistence.entity.RefreshToken;
import com.hubsi.authmicroservice.adapters.out.persistence.entity.User;
import com.hubsi.authmicroservice.adapters.out.persistence.repository.RefreshTokenRepository;
import com.hubsi.authmicroservice.adapters.out.persistence.repository.UserRepository;
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
public class AuthService {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
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

        String jwtToken = jwtService.generateToken(user);
        final Duration refreshTokenTtl = Duration.ofDays(7);
        final RefreshToken refreshToken = saveRefreshToken(refreshTokenTtl, user);

        return new AuthResponse(jwtToken, refreshToken.getId());
    }

    private RefreshToken saveRefreshToken(Duration refreshTokenTtl, User user) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setExpiresAt(Instant.now().plus(refreshTokenTtl));
        return refreshTokenRepository.save(refreshToken);
    }

    public AuthResponse refreshToken(UUID refreshToken) {
        final var refreshTokenEntity = refreshTokenRepository
                .findByIdAndExpiresAtAfter(refreshToken, Instant.now())
                .orElseThrow(() -> new UsernameNotFoundException("Refresh token inválido ou expirado!"));

        final var newAccessToken = jwtService.generateToken(refreshTokenEntity.getUser());

        return new AuthResponse(newAccessToken, refreshToken);
    }

    public void revokeRefreshToken(UUID refreshToken) {
        refreshTokenRepository.deleteById(refreshToken);
    }
}
