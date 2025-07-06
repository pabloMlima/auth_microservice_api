package com.hubsi.authmicroservice.application.services;

import com.hubsi.authmicroservice.adapters.out.impl.EmailAdapter;
import com.hubsi.authmicroservice.adapters.out.persistence.entity.ResetPassword;
import com.hubsi.authmicroservice.adapters.out.persistence.entity.User;
import com.hubsi.authmicroservice.adapters.out.persistence.repository.ResetPasswordRepository;
import com.hubsi.authmicroservice.adapters.out.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResetPasswordService {

    private final ResetPasswordRepository resetPasswordRepository;

    private final EmailAdapter emailAdapter;

    private final UserRepository userRepository;

    private final JwtService jwtService;

    public void resetPassword(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        String jwtToken = jwtService.generateToken(user);
        ResetPassword resetPassword = ResetPassword.builder()
                .token(jwtToken)
                .user(user)
                .usado(false)
                .expirado(false)
                .expirationTime(java.time.Instant.now().plusSeconds(900)) // 10 minutes expiration
                .build();

        resetPasswordRepository.save(resetPassword);

        emailAdapter.sendEmail(
                user.getEmail(),
                "Reset Password",
                "To reset your password, please click the link below:\n" +
                "http://localhost:8080/reset-password?token=" + jwtToken
        );
    }

}
