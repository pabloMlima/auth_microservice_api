package com.hubsi.authmicroservice.adapters.out.facades;

import com.hubsi.authmicroservice.adapters.out.EmailAdapter;
import com.hubsi.authmicroservice.adapters.out.persistence.entities.ResetPasswordEntity;
import com.hubsi.authmicroservice.adapters.out.persistence.entities.UserEntity;
import com.hubsi.authmicroservice.adapters.out.persistence.repository.JpaResetPasswordRepository;
import com.hubsi.authmicroservice.application.usecases.JwtUseCases;
import com.hubsi.authmicroservice.application.usecases.UserUseCases;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PasswordResetFacade {

    private final JpaResetPasswordRepository jpaResetPasswordRepository;

    private final EmailAdapter emailAdapter;

    private final UserUseCases userUseCases;

    private final JwtUseCases jwtUseCases;

    public void initiatePasswordReset(String email) {
        UserEntity userEntity = userUseCases.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        String jwtToken = jwtUseCases.generateToken(userEntity);
        ResetPasswordEntity resetPasswordEntity = ResetPasswordEntity.builder()
                .token(jwtToken)
                .userEntity(userEntity)
                .usado(false)
                .expirado(false)
                .expirationTime(java.time.Instant.now().plusSeconds(900)) // 10 minutes expiration
                .build();

        jpaResetPasswordRepository.save(resetPasswordEntity);

        emailAdapter.sendEmail(
                userEntity.getEmail(),
                "Reset Password",
                "To reset your password, please click the link below:\n" +
                        "http://localhost:8080/reset-password?token=" + jwtToken
        );
    }
}
