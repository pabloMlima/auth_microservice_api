package com.hubsi.authmicroservice.adapters.out.persistence.repository.impl;

import com.hubsi.authmicroservice.adapters.out.persistence.entities.RefreshTokenEntity;
import com.hubsi.authmicroservice.adapters.out.persistence.repository.JpaRefreshTokenRepository;
import com.hubsi.authmicroservice.domain.refresh_token.RefreshToken;
import com.hubsi.authmicroservice.domain.refresh_token.RefreshTokenRepository;
import com.hubsi.authmicroservice.utils.mappers.RefreshTokenMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class RefreshTokenImpl implements RefreshTokenRepository {

    private final JpaRefreshTokenRepository jpaRefreshTokenRepository;

    private final RefreshTokenMapper refreshTokenMapper;

    @Override
    public Optional<RefreshToken> findByIdAndExpiresAtAfter(UUID id, Instant date) {
        return jpaRefreshTokenRepository.findByIdAndExpiresAtAfter(id, date)
                .map(refreshTokenMapper::toDomain);
    }

    @Override
    public RefreshToken save(RefreshToken refreshToken) {
        RefreshTokenEntity refreshTokenEntity = refreshTokenMapper.toEntity(refreshToken);
        RefreshTokenEntity savedEntity = jpaRefreshTokenRepository.save(refreshTokenEntity);
        return refreshTokenMapper.toDomain(savedEntity);
    }

    @Override
    public void deleteById(UUID id) {
        jpaRefreshTokenRepository.deleteById(id);
    }
}
