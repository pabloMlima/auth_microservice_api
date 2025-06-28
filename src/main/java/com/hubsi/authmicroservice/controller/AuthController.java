package com.hubsi.authmicroservice.controller;

import com.hubsi.authmicroservice.dto.request.LoginRequest;
import com.hubsi.authmicroservice.dto.request.RegisterRequest;
import com.hubsi.authmicroservice.dto.response.AuthResponse;
import com.hubsi.authmicroservice.dto.response.RegisterResponse;
import com.hubsi.authmicroservice.entity.User;
import com.hubsi.authmicroservice.repository.UserRepository;
import com.hubsi.authmicroservice.services.AuthService;
import com.hubsi.authmicroservice.services.JwtService;
import com.hubsi.authmicroservice.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(userService.registerUser(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.authenticateUser(request.email(), request.password()));
    }
}