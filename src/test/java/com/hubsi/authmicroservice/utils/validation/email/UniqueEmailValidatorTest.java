package com.hubsi.authmicroservice.utils.validation.email;

import com.hubsi.authmicroservice.adapters.out.persistence.entities.UserEntity;
import com.hubsi.authmicroservice.adapters.out.persistence.repository.JpaUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UniqueEmailValidatorTest {

    private JpaUserRepository jpaUserRepository;
    private UniqueEmailValidator validator;

    @BeforeEach
    void setUp() {
        jpaUserRepository = mock(JpaUserRepository.class);
        validator = new UniqueEmailValidator(jpaUserRepository);
    }

    @Test
    void shouldReturnTrueWhenEmailIsUnique() {
        String email = "unique@email.com";
        when(jpaUserRepository.findByEmail(email)).thenReturn(Optional.empty());

        boolean result = validator.isValid(email, null);

        assertTrue(result);
    }

    @Test
    void shouldReturnFalseWhenEmailExists() {
        String email = "existing@email.com";
        when(jpaUserRepository.findByEmail(email)).thenReturn(Optional.of(new UserEntity()));

        boolean result = validator.isValid(email, null);

        assertFalse(result);
    }

    @Test
    void shouldReturnFalseWhenEmailIsNull() {
        boolean result = validator.isValid(null, null);

        assertFalse(result);
        verify(jpaUserRepository, never()).findByEmail(any());
    }
}