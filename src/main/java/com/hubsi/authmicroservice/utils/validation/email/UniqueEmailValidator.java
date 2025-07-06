package com.hubsi.authmicroservice.utils.validation.email;

import com.hubsi.authmicroservice.adapters.out.persistence.repository.JpaUserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    private final JpaUserRepository jpaUserRepository;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        return email != null && jpaUserRepository.findByEmail(email).isEmpty();
    }
}