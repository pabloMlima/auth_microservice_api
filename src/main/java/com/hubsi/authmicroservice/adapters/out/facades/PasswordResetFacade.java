package com.hubsi.authmicroservice.adapters.out.facades;

import com.hubsi.authmicroservice.adapters.out.impl.EmailAdapter;
import com.hubsi.authmicroservice.adapters.out.persistence.entities.ResetPassword;
import com.hubsi.authmicroservice.adapters.out.persistence.entities.User;
import com.hubsi.authmicroservice.adapters.out.persistence.repository.JpaResetPasswordRepository;
import com.hubsi.authmicroservice.application.services.JwtService;
import com.hubsi.authmicroservice.application.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PasswordResetFacade {

    private final JpaResetPasswordRepository jpaResetPasswordRepository;

    private final EmailAdapter emailAdapter;

    private final UserService userService;

    private final JwtService jwtService;

    public void initiatePasswordReset(String email) {
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        String jwtToken = jwtService.generateToken(user);
        ResetPassword resetPassword = ResetPassword.builder()
                .token(jwtToken)
                .user(user)
                .usado(false)
                .expirado(false)
                .expirationTime(java.time.Instant.now().plusSeconds(900)) // 10 minutes expiration
                .build();

        jpaResetPasswordRepository.save(resetPassword);

        emailAdapter.sendEmail(
                user.getEmail(),
                "Reset Password",
                "To reset your password, please click the link below:\n" +
                        "http://localhost:8080/reset-password?token=" + jwtToken
        );
    }
}
