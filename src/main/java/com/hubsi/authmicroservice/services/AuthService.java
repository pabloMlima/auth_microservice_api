package com.hubsi.authmicroservice.services;

import com.hubsi.authmicroservice.dto.response.AuthResponse;
import com.hubsi.authmicroservice.entity.User;
import com.hubsi.authmicroservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;


    public AuthResponse authenticateUser(String email, String password) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        email,
                        password
                )
        );
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado!"));

        String jwtToken = jwtService.generateToken(user);
        return new AuthResponse(jwtToken, "Login realizado com sucesso!");
    }
}
