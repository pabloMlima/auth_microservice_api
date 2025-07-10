package com.hubsi.authmicroservice.adapters.out.persistence.repository;

import com.hubsi.authmicroservice.adapters.out.persistence.entities.ResetPasswordEntity;
import com.hubsi.authmicroservice.adapters.out.persistence.repository.impl.ResetPasswordRepositoryImpl;
import com.hubsi.authmicroservice.domain.reset_password.ResetPassword;
import com.hubsi.authmicroservice.utils.mappers.ResetPasswordMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ResetPasswordRepositoryImplTest {

    private ResetPasswordMapper resetPasswordMapper;
    private JpaResetPasswordRepository jpaResetPasswordRepository;
    private ResetPasswordRepositoryImpl resetPasswordRepositoryImpl;

    @BeforeEach
    void setUp() {
        resetPasswordMapper = mock(ResetPasswordMapper.class);
        jpaResetPasswordRepository = mock(JpaResetPasswordRepository.class);
        resetPasswordRepositoryImpl = new ResetPasswordRepositoryImpl(resetPasswordMapper, jpaResetPasswordRepository);
    }

    @Test
    void testSave() {
        ResetPassword resetPassword = mock(ResetPassword.class);
        ResetPasswordEntity entity = mock(ResetPasswordEntity.class);
        ResetPasswordEntity savedEntity = mock(ResetPasswordEntity.class);
        ResetPassword expectedDomain = mock(ResetPassword.class);

        when(resetPasswordMapper.toEntity(resetPassword)).thenReturn(entity);
        when(jpaResetPasswordRepository.save(entity)).thenReturn(savedEntity);
        when(resetPasswordMapper.toDomain(savedEntity)).thenReturn(expectedDomain);

        ResetPassword result = resetPasswordRepositoryImpl.save(resetPassword);

        assertEquals(expectedDomain, result);
        verify(resetPasswordMapper).toEntity(resetPassword);
        verify(jpaResetPasswordRepository).save(entity);
        verify(resetPasswordMapper).toDomain(savedEntity);
    }
}