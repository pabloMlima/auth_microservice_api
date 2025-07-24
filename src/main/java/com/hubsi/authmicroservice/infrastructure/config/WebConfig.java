package com.hubsi.authmicroservice.infrastructure.config;

import com.hubsi.authmicroservice.infrastructure.constants.RoutesConstants;
import com.hubsi.authmicroservice.infrastructure.interceptors.CheckTokenResetPasswordInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final CheckTokenResetPasswordInterceptor resetPasswordTokenInterceptor;

    /**
     * Configura os interceptadores do Spring MVC.
     * Adiciona o interceptor de verificação de token de redefinição de senha para a rota de atualização de senha.
     *
     * @param registry O registro de interceptadores
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(resetPasswordTokenInterceptor)
                .addPathPatterns(RoutesConstants.USER_UPDATE_PASSWORD_SECURITY);
    }
}