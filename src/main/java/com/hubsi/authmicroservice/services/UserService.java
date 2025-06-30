package com.hubsi.authmicroservice.services;

import com.hubsi.authmicroservice.dto.request.RegisterRequest;
import com.hubsi.authmicroservice.dto.response.RegisterResponse;
import com.hubsi.authmicroservice.entity.User;
import com.hubsi.authmicroservice.enums.Role;
import com.hubsi.authmicroservice.mapper.UserMapper;
import com.hubsi.authmicroservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final JwtService jwtService;

    public UserDetails loadUserByUsername(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

    public RegisterResponse registerUser(RegisterRequest request) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String passCrypt = passwordEncoder.encode(request.password());
        User userSave = userMapper.toEntity(request, passCrypt, Role.USER);

        User user =  userRepository.save(userSave);
        String jwtToken = jwtService.generateToken(user);
        String message = "Usuário cadastrado com sucesso!";

        return userMapper.entityToDtoRegister(user, jwtToken, message);
    }

    public CompletableFuture<Optional<User>> findByEmail(String email) {
        return CompletableFuture.supplyAsync(() -> {
            log.info("Tentando encontrar usuário por email: " + email);
            return userRepository.findByEmail(email);
        });
    }
}