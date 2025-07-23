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

    private boolean isValidToken(String token) {
        return jwtUseCases.isTokenValid(token);
    }
}