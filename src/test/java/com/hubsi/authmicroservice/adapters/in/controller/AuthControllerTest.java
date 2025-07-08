package com.hubsi.authmicroservice.adapters.in.controller;

import com.hubsi.authmicroservice.adapters.in.request.LoginRequest;
import com.hubsi.authmicroservice.adapters.out.response.AuthResponse;
import com.hubsi.authmicroservice.application.usecases.AuthUseCases;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @Mock
    private AuthUseCases authUseCases;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLogin() {
        // Arrange
        LoginRequest request = new LoginRequest("test@example.com", "password123");
        AuthResponse expectedResponse = new AuthResponse("jwtToken", UUID.randomUUID());
        when(authUseCases.authenticateUser(request.email(), request.password())).thenReturn(expectedResponse);

        // Act
        ResponseEntity<AuthResponse> response = authController.login(request);

        // Assert
        assertEquals(ResponseEntity.ok(expectedResponse), response);
        verify(authUseCases, times(1)).authenticateUser(request.email(), request.password());
    }

    @Test
    void testRefreshToken() {
        // Arrange
        UUID refreshToken = UUID.randomUUID();
        AuthResponse expectedResponse = new AuthResponse("newJwtToken", refreshToken);
        when(authUseCases.refreshToken(refreshToken)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<AuthResponse> response = authController.refreshToken(refreshToken);

        // Assert
        assertEquals(ResponseEntity.ok(expectedResponse), response);
        verify(authUseCases, times(1)).refreshToken(refreshToken);
    }

    @Test
    void testRevokeToken() {
        // Arrange
        UUID refreshToken = UUID.randomUUID();
        doNothing().when(authUseCases).revokeRefreshToken(refreshToken);

        // Act
        ResponseEntity<Void> response = authController.revokeToken(refreshToken);

        // Assert
        assertEquals(ResponseEntity.noContent().build(), response);
        verify(authUseCases, times(1)).revokeRefreshToken(refreshToken);
    }

    @Test
    void testLoginWithInvalidCredentials() {
        // Arrange
        LoginRequest request = new LoginRequest("invalid@example.com", "wrongPassword");
        when(authUseCases.authenticateUser(request.email(), request.password()))
                .thenThrow(new RuntimeException("Invalid credentials"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authController.login(request);
        });
        assertEquals("Invalid credentials", exception.getMessage());
        verify(authUseCases, times(1)).authenticateUser(request.email(), request.password());
    }

    @Test
    void testRefreshTokenWithInvalidToken() {
        // Arrange
        UUID invalidRefreshToken = UUID.randomUUID();
        when(authUseCases.refreshToken(invalidRefreshToken))
                .thenThrow(new RuntimeException("Invalid refresh token"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authController.refreshToken(invalidRefreshToken);
        });
        assertEquals("Invalid refresh token", exception.getMessage());
        verify(authUseCases, times(1)).refreshToken(invalidRefreshToken);
    }

    @Test
    void testRevokeTokenWithException() {
        // Arrange
        UUID refreshToken = UUID.randomUUID();
        doThrow(new RuntimeException("Error revoking token"))
                .when(authUseCases).revokeRefreshToken(refreshToken);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authController.revokeToken(refreshToken);
        });
        assertEquals("Error revoking token", exception.getMessage());
        verify(authUseCases, times(1)).revokeRefreshToken(refreshToken);
    }

}