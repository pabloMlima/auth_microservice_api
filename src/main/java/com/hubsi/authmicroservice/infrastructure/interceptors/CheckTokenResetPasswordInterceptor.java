package com.hubsi.authmicroservice.infrastructure.interceptors;

import com.hubsi.authmicroservice.application.usecases.JwtUseCases;
import com.hubsi.authmicroservice.infrastructure.constants.RoutesConstants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class CheckTokenResetPasswordInterceptor implements HandlerInterceptor {

    private final JwtUseCases jwtUseCases;

    /**
     * Intercepta requisições para verificar a validade do token de redefinição de senha.
     * Se o token for inválido ou não estiver presente, retorna um status 401 (Unauthorized).
     *
     * @param request  A requisição HTTP
     * @param response A resposta HTTP
     * @param handler  O manipulador do request
     * @return true se o token for válido, false caso contrário
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (request.getRequestURI().contains(RoutesConstants.USER_UPDATE_PASSWORD)) {
            String token = request.getParameter("token");
            if (token == null || !isValidToken(token)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return false;
            }
        }
        return true;
    }

    /**
     * Verifica se o token fornecido é válido.
     *
     * @param token O token a ser verificado
     * @return true se o token for válido, false caso contrário
     */
    private boolean isValidToken(String token) {
        return jwtUseCases.isTokenValid(token);
    }
}