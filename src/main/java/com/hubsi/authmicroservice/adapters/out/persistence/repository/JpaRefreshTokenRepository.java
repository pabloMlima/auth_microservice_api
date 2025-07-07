package com.hubsi.authmicroservice.adapters.out.persistence.repository;

import com.hubsi.authmicroservice.adapters.out.persistence.entities.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaRefreshTokenRepository extends JpaRepository<RefreshTokenEntity, UUID> {

    Optional<RefreshTokenEntity> findByIdAndExpiresAtAfter(UUID id, Instant date);
}