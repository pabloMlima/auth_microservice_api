package com.hubsi.authmicroservice.application.services;

import com.hubsi.authmicroservice.adapters.in.request.RegisterRequest;
import com.hubsi.authmicroservice.adapters.out.persistence.entities.UserEntity;
import com.hubsi.authmicroservice.adapters.out.response.RegisterResponse;
import com.hubsi.authmicroservice.adapters.out.security.UserDetailsImpl;
import com.hubsi.authmicroservice.application.usecases.UserUseCases;
import com.hubsi.authmicroservice.domain.user.User;
import com.hubsi.authmicroservice.domain.user.UserRepository;
import com.hubsi.authmicroservice.utils.enums.Role;
import com.hubsi.authmicroservice.utils.mappers.UserMapper;
import com.hubsi.authmicroservice.adapters.out.persistence.repository.JpaResetPasswordRepository;
import com.hubsi.authmicroservice.adapters.out.persistence.repository.JpaUserRepository;
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

    private final JpaUserRepository jpaUserRepository;

    private final UserMapper userMapper;

    private final JwtService jwtService;

    private final JpaResetPasswordRepository jpaResetPasswordRepository;

    private final UserRepository userRepository;

    public UserDetails loadUserByUsername(String email) {
        return jpaUserRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

    /*
    public RegisterResponse registerUser(RegisterRequest request) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String passCrypt = passwordEncoder.encode(request.password());
        UserEntity userEntitySave = userMapper.toEntity(request, passCrypt, Role.USER);

        UserEntity userEntity =  jpaUserRepository.save(userEntitySave);
        String jwtToken = jwtService.generateToken(userEntity);
        String message = "Usuário cadastrado com sucesso!";

        return userMapper.entityToDtoRegister(userEntity, jwtToken, message);
    }

     */

    public RegisterResponse registerUser(RegisterRequest request) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String passCrypt = passwordEncoder.encode(request.password());

        User user = userMapper.dtoToDomain(request, passCrypt, Role.USER);
        User userSave = userRepository.save(user);
        UserDetailsImpl userDetails = new UserDetailsImpl(userSave);

        String jwtToken = jwtService.generateToken(userDetails);
        String message = "Usuário cadastrado com sucesso!";

        return userMapper.entityToDtoRegister(userSave, jwtToken, message);
    }

    public Optional<UserEntity> findByEmail(String email) {
        return jpaUserRepository.findByEmail(email);
    }
}