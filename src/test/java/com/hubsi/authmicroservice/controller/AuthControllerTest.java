package com.hubsi.authmicroservice.controller;

import com.hubsi.authmicroservice.adapters.in.controller.AuthController;
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

}