package com.hubsi.authmicroservice.application.services;

import com.hubsi.authmicroservice.adapters.in.request.RegisterRequest;
import com.hubsi.authmicroservice.adapters.out.response.RegisterResponse;
import com.hubsi.authmicroservice.adapters.out.persistence.entity.ResetPassword;
import com.hubsi.authmicroservice.adapters.out.persistence.entity.User;
import com.hubsi.authmicroservice.application.usecases.UserUseCases;
import com.hubsi.authmicroservice.utils.enums.Role;
import com.hubsi.authmicroservice.utils.mappers.UserMapper;
import com.hubsi.authmicroservice.adapters.out.persistence.repository.ResetPasswordRepository;
import com.hubsi.authmicroservice.adapters.out.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
@Log4j2
public class UserService implements UserDetailsService, UserUseCases {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final JwtService jwtService;

    private final ResetPasswordRepository resetPasswordRepository;

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

    public void resetPassword(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        String jwtToken = jwtService.generateToken(user);
        ResetPassword resetPassword = ResetPassword.builder()
                .token(jwtToken)
                .user(user)
                .usado(false)
                .expirado(false)
                .expirationTime(java.time.Instant.now().plusSeconds(900)) // 10 minutes expiration
                .build();

        resetPasswordRepository.save(resetPassword);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}