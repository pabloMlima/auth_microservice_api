package com.hubsi.authmicroservice.application.usecases;

import com.hubsi.authmicroservice.adapters.in.request.RegisterRequest;
import com.hubsi.authmicroservice.adapters.out.persistence.entities.UserEntity;
import com.hubsi.authmicroservice.adapters.out.response.RegisterResponse;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UserUseCases {

    UserDetails loadUserByUsername(String email);

    RegisterResponse registerUser(RegisterRequest request);

    Optional<UserEntity> findByEmail(String email);
}
