package com.hubsi.authmicroservice.infrastructure.config;

import com.hubsi.authmicroservice.application.usecases.JwtUseCases;
import com.hubsi.authmicroservice.application.usecases.UserUseCases;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import static org.mockito.Mockito.*;

class JwtAuthenticationFilterTest {

    @Mock
    private JwtUseCases jwtUseCases;
    @Mock
    private UserUseCases userUseCases;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private FilterChain filterChain;
    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private JwtAuthenticationFilter filter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.clearContext();
    }

    @Test
    void deveIgnorarQuandoHeaderAusente() throws Exception {
        when(request.getHeader("Authorization")).thenReturn(null);

        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(jwtUseCases, userUseCases);
    }

    @Test
    void deveAutenticarQuandoTokenValido() throws Exception {
        String token = "valid.jwt.token";
        String email = "user@email.com";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtUseCases.extractUsername(token)).thenReturn(email);
        when(userUseCases.loadUserByUsername(email)).thenReturn(userDetails);
        when(jwtUseCases.isTokenValid(token, userDetails)).thenReturn(true);
        when(userDetails.getAuthorities()).thenReturn(new java.util.ArrayList<>());

        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        // Verifica se a autenticação foi setada no contexto
        assert(SecurityContextHolder.getContext().getAuthentication() != null);
    }
}
