package com.hubsi.authmicroservice.adapters.out.persistence.repository.impl;

import com.hubsi.authmicroservice.adapters.out.persistence.entities.ResetPasswordEntity;
import com.hubsi.authmicroservice.adapters.out.persistence.repository.JpaResetPasswordRepository;
import com.hubsi.authmicroservice.domain.reset_password.ResetPassword;
import com.hubsi.authmicroservice.domain.reset_password.ResetPasswordRepository;
import com.hubsi.authmicroservice.utils.mappers.ResetPasswordMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ResetPasswordRepositoryImpl implements ResetPasswordRepository {

    private final ResetPasswordMapper resetPasswordMapper;

    private final JpaResetPasswordRepository jpaResetPasswordRepository;

    @Override
    public ResetPassword save(ResetPassword resetPassword) {
        ResetPasswordEntity resetPasswordEntity = jpaResetPasswordRepository.save(
                resetPasswordMapper.toEntity(resetPassword)
        );

        return resetPasswordMapper.toDomain(resetPasswordEntity);
    }
}
