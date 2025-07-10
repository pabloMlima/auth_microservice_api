package com.hubsi.authmicroservice.application.usecases;

import com.hubsi.authmicroservice.adapters.in.request.RegisterRequest;
import com.hubsi.authmicroservice.adapters.out.response.RegisterResponse;
import com.hubsi.authmicroservice.domain.user.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UserUseCases {

    UserDetails loadUserByUsername(String email);

    RegisterResponse registerUser(RegisterRequest request);

    Optional<User> findByEmail(String email);
}
