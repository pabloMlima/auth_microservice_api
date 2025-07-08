package com.hubsi.authmicroservice.application.services;

import com.hubsi.authmicroservice.adapters.out.security.UserDetailsImpl;
import com.hubsi.authmicroservice.application.port.out.EmailAdapter;
import com.hubsi.authmicroservice.application.usecases.ResetPasswordUseCases;
import com.hubsi.authmicroservice.domain.reset_password.ResetPassword;
import com.hubsi.authmicroservice.domain.reset_password.ResetPasswordRepository;
import com.hubsi.authmicroservice.domain.user.User;
import com.hubsi.authmicroservice.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResetPasswordService implements ResetPasswordUseCases {

    private final EmailAdapter emailAdapter;

    private final JwtService jwtService;

    private final UserRepository userRepository;

    private final ResetPasswordRepository resetPasswordRepository;

    public void resetPassword(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        UserDetailsImpl userDetails = new UserDetailsImpl(user);
        String jwtToken = jwtService.generateToken(userDetails);

        ResetPassword resetPassword = new ResetPassword(
                user,
                jwtToken,
                false,
                false,
                java.time.Instant.now().plusSeconds(900) // 10 minutes expiration
        );

        resetPasswordRepository.save(resetPassword);

        emailAdapter.sendEmail(
                user.getEmail(),
                "Reset Password",
                "To reset your password, please click the link below:\n" +
                "http://localhost:8080/reset-password?token=" + jwtToken
        );
    }

}
