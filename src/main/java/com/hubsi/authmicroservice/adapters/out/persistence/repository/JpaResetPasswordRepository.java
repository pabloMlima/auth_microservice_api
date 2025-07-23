package com.hubsi.authmicroservice.adapters.out.persistence.repository;

import com.hubsi.authmicroservice.adapters.out.persistence.entities.ResetPasswordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaResetPasswordRepository extends JpaRepository<ResetPasswordEntity, UUID> {

    Optional<ResetPasswordEntity> findByToken(String token);
}
