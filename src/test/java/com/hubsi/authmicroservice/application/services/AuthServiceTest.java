package com.hubsi.authmicroservice.application.services;

import com.hubsi.authmicroservice.adapters.out.response.AuthResponse;
import com.hubsi.authmicroservice.application.usecases.JwtUseCases;
import com.hubsi.authmicroservice.domain.refresh_token.RefreshToken;
import com.hubsi.authmicroservice.domain.refresh_token.RefreshTokenRepository;
import com.hubsi.authmicroservice.domain.user.User;
import com.hubsi.authmicroservice.domain.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUseCases jwtUseCases;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoginWithInvalidCredentials() {
        // Arrange
        String email = "invalid@example.com";
        String password = "wrongPassword";

        doThrow(new RuntimeException("Invalid credentials"))
                .when(authenticationManager).authenticate(any());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.authenticateUser(email, password);
        });
        assertEquals("Invalid credentials", exception.getMessage());
        verify(authenticationManager, times(1)).authenticate(any());
    }

    @Test
    void testRefreshTokenWithInvalidToken() {
        // Arrange
        UUID invalidRefreshToken = UUID.randomUUID();
        when(refreshTokenRepository.findByIdAndExpiresAtAfter(eq(invalidRefreshToken), any()))
                .thenReturn(java.util.Optional.empty());

        // Act & Assert
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            authService.refreshToken(invalidRefreshToken);
        });
        assertEquals("Refresh token inválido ou expirado!", exception.getMessage());
        verify(refreshTokenRepository, times(1)).findByIdAndExpiresAtAfter(eq(invalidRefreshToken), any());
    }

    @Test
    void testRevokeTokenWithException() {
        // Arrange
        UUID refreshToken = UUID.randomUUID();

        doThrow(new RuntimeException("Error revoking token"))
                .when(refreshTokenRepository).deleteById(refreshToken);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.revokeRefreshToken(refreshToken);
        });
        assertEquals("Error revoking token", exception.getMessage());
        verify(refreshTokenRepository, times(1)).deleteById(refreshToken);
    }

    @Test
    void testAuthenticateUserWithValidCredentials() {
        // Arrange
        String email = "valid@example.com";
        String password = "correctPassword";
        User user = new User(); // Mocked user object
        user.setEmail(email);

        RefreshToken mockRefreshToken = new RefreshToken(user, Instant.now(), Instant.now().plus(Duration.ofDays(7)));
        mockRefreshToken.setId(UUID.randomUUID()); // Ensure the ID is not null

        when(authenticationManager.authenticate(any())).thenReturn(null);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(jwtUseCases.generateToken(any())).thenReturn("mockJwtToken");
        when(refreshTokenRepository.save(any())).thenReturn(mockRefreshToken);

        // Act
        AuthResponse response = authService.authenticateUser(email, password);

        // Assert
        assertEquals("mockJwtToken", response.token());
        assertNotNull(response.refreshToken()); // This should now pass
        verify(authenticationManager, times(1)).authenticate(any());
        verify(userRepository, times(1)).findByEmail(email);
        verify(jwtUseCases, times(1)).generateToken(any());
        verify(refreshTokenRepository, times(1)).save(any());
    }

    @Test
    void testRefreshTokenWithValidToken() {
        // Arrange
        UUID validRefreshToken = UUID.randomUUID();
        User user = new User();
        RefreshToken refreshToken = new RefreshToken(user, Instant.now(), Instant.now().plus(Duration.ofDays(7)));
        refreshToken.setId(validRefreshToken);

        when(refreshTokenRepository.findByIdAndExpiresAtAfter(eq(validRefreshToken), any())).thenReturn(Optional.of(refreshToken));
        when(jwtUseCases.generateToken(any())).thenReturn("newMockJwtToken");

        // Act
        AuthResponse response = authService.refreshToken(validRefreshToken);

        // Assert
        assertEquals("newMockJwtToken", response.token());
        assertEquals(validRefreshToken, response.refreshToken());
        verify(refreshTokenRepository, times(1)).findByIdAndExpiresAtAfter(eq(validRefreshToken), any());
        verify(jwtUseCases, times(1)).generateToken(any());
    }

    @Test
    void testSaveRefreshToken() {
        // Arrange
        User user = new User(); // Mocked user object
        Duration ttl = Duration.ofDays(7);
        RefreshToken refreshToken = new RefreshToken(user, Instant.now(), Instant.now().plus(ttl));

        when(refreshTokenRepository.save(any())).thenReturn(refreshToken);

        // Act
        RefreshToken savedToken = authService.saveRefreshToken(ttl, user);

        // Assert
        assertNotNull(savedToken);
        assertEquals(user, savedToken.getUser());
        verify(refreshTokenRepository, times(1)).save(any());
    }

    @Test
    void testRevokeRefreshToken() {
        // Arrange
        UUID refreshToken = UUID.randomUUID();

        // Act
        authService.revokeRefreshToken(refreshToken);

        // Assert
        verify(refreshTokenRepository, times(1)).deleteById(refreshToken);

        // No exception should be thrown, so we don't need an assert here
        // If an exception is thrown, the test will fail automatically
        // This is a simple test to ensure that the method can be called without exceptions
        assertDoesNotThrow(() -> authService.revokeRefreshToken(refreshToken));

    }
}