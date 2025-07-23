package com.hubsi.authmicroservice.application.services;

import com.hubsi.authmicroservice.adapters.out.security.UserDetailsImpl;
import com.hubsi.authmicroservice.application.usecases.JwtUseCases;
import com.hubsi.authmicroservice.infrastructure.port.out.EmailAdapter;
import com.hubsi.authmicroservice.application.usecases.ResetPasswordUseCases;
import com.hubsi.authmicroservice.domain.reset_password.ResetPassword;
import com.hubsi.authmicroservice.domain.reset_password.ResetPasswordRepository;
import com.hubsi.authmicroservice.domain.user.User;
import com.hubsi.authmicroservice.domain.user.UserRepository;
import com.hubsi.authmicroservice.infrastructure.exceptions.ResetPasswordTokenException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ResetPasswordService implements ResetPasswordUseCases {

    private final EmailAdapter emailAdapter;

    private final JwtUseCases jwtUseCases;

    private final UserRepository userRepository;

    private final ResetPasswordRepository resetPasswordRepository;

    @Value("${spring.application.frontend.url}")
    private String urlFront;

    @Override
    public void resetPassword(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        UserDetailsImpl userDetails = new UserDetailsImpl(user);
        String jwtToken = jwtUseCases.generateToken(userDetails);

        ResetPassword resetPassword = new ResetPassword(
                user,
                jwtToken,
                false,
                false,
                java.time.Instant.now().plusSeconds(900) // 10 minutes expiration
        );

        resetPasswordRepository.save(resetPassword);

        String urlResetPassword = String.format("%s/reset-password?token=%s", this.urlFront, jwtToken);
        String body = String.format(
                "Olá %s,%n%nPara redefinir sua senha, por favor clique no link abaixo:%n%s%n%nEste link é válido por 10 minutos.",
                user.getNome(), urlResetPassword
        );

        emailAdapter.sendEmail(
                user.getEmail(),
                "Reset Password",
                body
        );
    }

    @Override
    public void confirmResetPassword(String token, String newPassword) {
        Optional<ResetPassword> resetPassword = resetPasswordRepository.findByToken(token);
        validateToken(resetPassword);

        resetPassword.ifPresent(reset -> {
            User user = reset.getUser();
            user.setPassword(newPassword);
            userRepository.save(user);

            reset.setUsado(true);
            resetPasswordRepository.save(reset);
        });
    }

    private boolean isTokenExpired(Instant expirationTime) {
        return expirationTime.isBefore(java.time.Instant.now());
    }

    private void validateToken(Optional<ResetPassword> resetPassword) {
        resetPassword.map(reset -> {
            if (reset.isUsado() || reset.isExpirado()) {
                throw new ResetPasswordTokenException("Token de redefinição de senha já foi usado ou expirou");
            }else if(isTokenExpired(reset.getExpirationTime())){
                reset.setExpirado(true);
                resetPasswordRepository.save(reset);
                throw new ResetPasswordTokenException("Token de redefinição de senha expirado");
            }
            return reset;
        }).orElseThrow(() -> new ResetPasswordTokenException("Token de redefinição de senha inválido ou não encontrado"));
    }

}
