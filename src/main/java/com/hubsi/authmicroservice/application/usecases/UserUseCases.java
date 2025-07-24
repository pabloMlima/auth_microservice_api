package com.hubsi.authmicroservice.application.usecases;

import com.hubsi.authmicroservice.adapters.in.request.RegisterRequest;
import com.hubsi.authmicroservice.adapters.out.response.RegisterResponse;
import com.hubsi.authmicroservice.domain.user.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UserUseCases {

    /**
     * Carrega os detalhes do usuário com base no e-mail fornecido.
     *
     * @param email o e-mail do usuário
     * @return os detalhes do usuário
     */
    UserDetails loadUserByUsername(String email);

    /**
     * Registra um novo usuário com base na solicitação fornecida.
     *
     * @param request a solicitação de registro contendo os detalhes do usuário
     * @return a resposta do registro contendo informações sobre o usuário registrado
     */
    RegisterResponse registerUser(RegisterRequest request);

    /**
     * Verifica se um usuário com o e-mail fornecido já existe.
     *
     * @param email o e-mail do usuário a ser verificado
     * @return true se o usuário existir, false caso contrário
     */
    Optional<User> findByEmail(String email);
}
