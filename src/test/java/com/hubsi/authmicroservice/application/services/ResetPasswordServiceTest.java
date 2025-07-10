package com.hubsi.authmicroservice.application.services;

import com.hubsi.authmicroservice.application.port.out.EmailAdapter;
import com.hubsi.authmicroservice.domain.reset_password.ResetPassword;
import com.hubsi.authmicroservice.domain.reset_password.ResetPasswordRepository;
import com.hubsi.authmicroservice.domain.user.User;
import com.hubsi.authmicroservice.domain.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ResetPasswordServiceTest {

    private EmailAdapter emailAdapter;
    private JwtService jwtService;
    private UserRepository userRepository;
    private ResetPasswordRepository resetPasswordRepository;
    private ResetPasswordService resetPasswordService;

    @BeforeEach
    void setUp() {
        emailAdapter = mock(EmailAdapter.class);
        jwtService = mock(JwtService.class);
        userRepository = mock(UserRepository.class);
        resetPasswordRepository = mock(ResetPasswordRepository.class);
        resetPasswordService = new ResetPasswordService(
                emailAdapter, jwtService, userRepository, resetPasswordRepository
        );
    }

    @Test
    void resetPassword_deveEnviarEmailQuandoUsuarioExiste() {
        String email = "teste@exemplo.com";
        User user = new User();
        user.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(jwtService.generateToken(any())).thenReturn("jwt-token");

        resetPasswordService.resetPassword(email);

        verify(resetPasswordRepository, times(1)).save(any(ResetPassword.class));
        verify(emailAdapter, times(1)).sendEmail(
                eq(email),
                eq("Reset Password"),
                contains("http://localhost:8080/reset-password?token=jwt-token")
        );
    }

    @Test
    void resetPassword_deveLancarExcecaoQuandoUsuarioNaoExiste() {
        String email = "naoexiste@exemplo.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            resetPasswordService.resetPassword(email);
        });

        verify(resetPasswordRepository, never()).save(any());
        verify(emailAdapter, never()).sendEmail(any(), any(), any());
    }
}