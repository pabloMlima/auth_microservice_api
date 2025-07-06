package com.hubsi.authmicroservice.repository;

import com.hubsi.authmicroservice.entity.ResetPassword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ResetPasswordRepository extends JpaRepository<ResetPassword, UUID> {
}
