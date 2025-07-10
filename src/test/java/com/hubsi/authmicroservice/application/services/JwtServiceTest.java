package com.hubsi.authmicroservice.application.services;

import com.hubsi.authmicroservice.application.usecases.JwtUseCases;
import com.hubsi.authmicroservice.infrastructure.exceptions.JwtInvalidException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.lang.reflect.Field;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class JwtServiceTest {

    private JwtUseCases jwtUseCases;

    @BeforeEach
    void setUp() throws Exception {
        JwtService jwtService = new JwtService();

        // Use reflection to set the private fields
        Field secretKeyField = JwtService.class.getDeclaredField("secretKey");
        secretKeyField.setAccessible(true);
        secretKeyField.set(jwtService, "E63513266556A586E3272357538782F413F4428472B4B6250645367566B5970213131");

        Field jwtExpirationField = JwtService.class.getDeclaredField("jwtExpiration");
        jwtExpirationField.setAccessible(true);
        jwtExpirationField.set(jwtService, 3600000L);

        jwtUseCases = jwtService;
    }

    @Test
    void testGenerateToken() {
        // Arrange
        UserDetails userDetails = User.withUsername("testuser").password("password").roles("USER").build();

        // Act
        String token = jwtUseCases.generateToken(userDetails);

        // Assert
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void testExtractUsername() {
        // Arrange
        UserDetails userDetails = User.withUsername("testuser").password("password").roles("USER").build();
        String token = jwtUseCases.generateToken(userDetails);

        // Act
        String username = jwtUseCases.extractUsername(token);

        // Assert
        assertEquals("testuser", username);
    }

    @Test
    void testIsTokenValid() {
        // Arrange
        UserDetails userDetails = User.withUsername("testuser").password("password").roles("USER").build();
        String token = jwtUseCases.generateToken(userDetails);

        // Act
        boolean isValid = jwtUseCases.isTokenValid(token, userDetails);

        // Assert
        assertTrue(isValid);
    }

    @Test
    void testIsTokenExpired() {
        // Arrange
        String token = Jwts.builder()
                .setSubject("user")
                .setExpiration(new Date(System.currentTimeMillis() - 1000 * 60)) // 1 minuto no passado
                .signWith(((JwtService) jwtUseCases).getSignInKey(), SignatureAlgorithm.HS256)
                .compact();

        // Act
        boolean isExpired = jwtUseCases.isTokenExpired(token);

        // Assert
        assertTrue(isExpired);
    }

    @Test
    void testExtractAllClaims() {
        // Arrange
        UserDetails userDetails = User.withUsername("testuser").password("password").roles("USER").build();
        String token = jwtUseCases.generateToken(userDetails);

        // Act
        var claims = jwtUseCases.extractAllClaims(token);

        // Assert
        assertNotNull(claims);
        assertEquals("testuser", claims.getSubject());
    }

    @Test
    void testExtractAllClaimsExpiredToken() {
        // Arrange
        String expiredToken = Jwts.builder()
                .setSubject("user")
                .setExpiration(new Date(System.currentTimeMillis() - 1000 * 60))
                .signWith(((JwtService) jwtUseCases).getSignInKey(), SignatureAlgorithm.HS256)
                .compact();

        // Act & Assert
        JwtInvalidException exception = assertThrows(JwtInvalidException.class, () -> {
            jwtUseCases.extractAllClaims(expiredToken);
        });
        assertTrue(exception.getMessage().contains("Token expirado"));
    }

    @Test
    void testExtractAllClaimsInvalidToken() {
        // Arrange
        String invalidToken = "invalid.token.value";

        // Act & Assert
        JwtInvalidException exception = assertThrows(JwtInvalidException.class, () -> {
            jwtUseCases.extractAllClaims(invalidToken);
        });
        assertTrue(exception.getMessage().contains("Token inválido"));
    }
}
