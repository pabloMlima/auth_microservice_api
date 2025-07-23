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

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(resetPasswordTokenInterceptor)
                .addPathPatterns("/api/v1/user/update-password");
    }
}