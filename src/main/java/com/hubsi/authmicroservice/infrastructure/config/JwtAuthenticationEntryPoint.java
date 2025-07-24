package com.hubsi.authmicroservice.infrastructure.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {


    /**
     * Método que lida com tentativas de acesso não autorizado.
     * Retorna um erro 401 (Unauthorized) com a mensagem da exceção de autenticação.
     *
     * @param request       A requisição HTTP
     * @param response      A resposta HTTP
     * @param authException A exceção de autenticação
     * @throws IOException      Se ocorrer um erro de entrada/saída
     * @throws ServletException Se ocorrer um erro de servlet
     */
    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {

        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());

    }
}