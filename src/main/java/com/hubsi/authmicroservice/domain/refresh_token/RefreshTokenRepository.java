package com.hubsi.authmicroservice.domain.refresh_token;


import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository {

    Optional<RefreshToken> findByIdAndExpiresAtAfter(UUID id, Instant date);

    RefreshToken save(RefreshToken refreshToken);
}
