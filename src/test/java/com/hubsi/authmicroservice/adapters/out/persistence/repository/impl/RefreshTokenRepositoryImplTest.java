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

    private JpaRefreshTokenRepository jpaRepository;
    private RefreshTokenMapper mapper;
    private RefreshTokenImpl refreshTokenImpl;

    @BeforeEach
    void setUp() {
        jpaRepository = mock(JpaRefreshTokenRepository.class);
        mapper = mock(RefreshTokenMapper.class);
        refreshTokenImpl = new RefreshTokenImpl(jpaRepository, mapper);
    }

    @Test
    void testFindByIdAndExpiresAtAfter_Found() {
        UUID id = UUID.randomUUID();
        Instant now = Instant.now();

        RefreshTokenEntity entity = new RefreshTokenEntity();
        RefreshToken domain = new RefreshToken();

        when(jpaRepository.findByIdAndExpiresAtAfter(id, now)).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(domain);

        Optional<RefreshToken> result = refreshTokenImpl.findByIdAndExpiresAtAfter(id, now);

        assertTrue(result.isPresent());
        assertEquals(domain, result.get());
        verify(jpaRepository).findByIdAndExpiresAtAfter(id, now);
        verify(mapper).toDomain(entity);
    }

    @Test
    void testFindByIdAndExpiresAtAfter_NotFound() {
        UUID id = UUID.randomUUID();
        Instant now = Instant.now();

        when(jpaRepository.findByIdAndExpiresAtAfter(id, now)).thenReturn(Optional.empty());

        Optional<RefreshToken> result = refreshTokenImpl.findByIdAndExpiresAtAfter(id, now);

        assertTrue(result.isEmpty());
        verify(jpaRepository).findByIdAndExpiresAtAfter(id, now);
        verify(mapper, never()).toDomain(any());
    }

    @Test
    void testSave() {
        RefreshToken domain = new RefreshToken();
        RefreshTokenEntity entity = new RefreshTokenEntity();
        RefreshTokenEntity savedEntity = new RefreshTokenEntity();
        RefreshToken expectedDomain = new RefreshToken();

        when(mapper.toEntity(domain)).thenReturn(entity);
        when(jpaRepository.save(entity)).thenReturn(savedEntity);
        when(mapper.toDomain(savedEntity)).thenReturn(expectedDomain);

        RefreshToken result = refreshTokenImpl.save(domain);

        assertEquals(expectedDomain, result);
        verify(mapper).toEntity(domain);
        verify(jpaRepository).save(entity);
        verify(mapper).toDomain(savedEntity);
    }

    @Test
    void testDeleteById() {
        UUID id = UUID.randomUUID();

        refreshTokenImpl.deleteById(id);

        verify(jpaRepository).deleteById(id);
    }
}