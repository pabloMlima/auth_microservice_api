package com.hubsi.authmicroservice.adapters.out.persistence.repository.impl;

import com.hubsi.authmicroservice.adapters.out.persistence.entities.RefreshTokenEntity;
import com.hubsi.authmicroservice.adapters.out.persistence.repository.JpaRefreshTokenRepository;
import com.hubsi.authmicroservice.domain.refresh_token.RefreshToken;
import com.hubsi.authmicroservice.utils.mappers.RefreshTokenMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RefreshTokenImplTest {

    private JpaRefreshTokenRepository jpaRefreshTokenRepository;
    private RefreshTokenMapper refreshTokenMapper;
    private RefreshTokenImpl refreshTokenImpl;

    @BeforeEach
    void setUp() {
        jpaRefreshTokenRepository = mock(JpaRefreshTokenRepository.class);
        refreshTokenMapper = mock(RefreshTokenMapper.class);
        refreshTokenImpl = new RefreshTokenImpl(jpaRefreshTokenRepository, refreshTokenMapper);
    }

    @Test
    void findByIdAndExpiresAtAfter_deveRetornarRefreshTokenQuandoEncontrado() {
        UUID id = UUID.randomUUID();
        Instant date = Instant.now();
        RefreshTokenEntity entity = new RefreshTokenEntity();
        RefreshToken domain = new RefreshToken();

        when(jpaRefreshTokenRepository.findByIdAndExpiresAtAfter(id, date)).thenReturn(Optional.of(entity));
        when(refreshTokenMapper.toDomain(entity)).thenReturn(domain);

        Optional<RefreshToken> result = refreshTokenImpl.findByIdAndExpiresAtAfter(id, date);

        assertTrue(result.isPresent());
        assertEquals(domain, result.get());
    }

    @Test
    void findByIdAndExpiresAtAfter_deveRetornarVazioQuandoNaoEncontrado() {
        UUID id = UUID.randomUUID();
        Instant date = Instant.now();

        when(jpaRefreshTokenRepository.findByIdAndExpiresAtAfter(id, date)).thenReturn(Optional.empty());

        Optional<RefreshToken> result = refreshTokenImpl.findByIdAndExpiresAtAfter(id, date);

        assertFalse(result.isPresent());
    }

    @Test
    void save_deveSalvarERetornarRefreshToken() {
        RefreshToken domain = new RefreshToken();
        RefreshTokenEntity entity = new RefreshTokenEntity();
        RefreshTokenEntity savedEntity = new RefreshTokenEntity();
        RefreshToken savedDomain = new RefreshToken();

        when(refreshTokenMapper.toEntity(domain)).thenReturn(entity);
        when(jpaRefreshTokenRepository.save(entity)).thenReturn(savedEntity);
        when(refreshTokenMapper.toDomain(savedEntity)).thenReturn(savedDomain);

        RefreshToken result = refreshTokenImpl.save(domain);

        assertEquals(savedDomain, result);
    }

    @Test
    void deleteById_deveChamarRepositorio() {
        UUID id = UUID.randomUUID();

        refreshTokenImpl.deleteById(id);

        verify(jpaRefreshTokenRepository, times(1)).deleteById(id);
    }
}