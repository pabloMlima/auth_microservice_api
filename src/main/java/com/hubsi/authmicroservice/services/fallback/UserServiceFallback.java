package com.hubsi.authmicroservice.services.fallback;

import com.hubsi.authmicroservice.dto.response.RegisterResponse;
import com.hubsi.authmicroservice.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component("userServiceFallback")
public class UserServiceFallback {

    public RegisterResponse registerUserFallback(User user, Throwable t) {
        log.error("Fallback acionado para registerUser: " + user.getEmail() + " - Erro: " + t.getMessage());
        return RegisterResponse.builder().message("Não foi possível registrar o usuário no momento. Tente novamente."+ t).build();
    }

    public RegisterResponse findByEmailFallback(String email, Throwable t) {
        log.error("Fallback acionado para findByEmail: " + email + " - Erro: " + t.getMessage());
        return RegisterResponse.builder().message("Não foi possível registrar o usuário no momento. Tente novamente."+ t).build();
    }
}