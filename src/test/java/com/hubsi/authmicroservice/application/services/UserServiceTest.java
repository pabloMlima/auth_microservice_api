package com.hubsi.authmicroservice.application.services;

import com.hubsi.authmicroservice.adapters.in.request.RegisterRequest;
import com.hubsi.authmicroservice.adapters.out.response.RegisterResponse;
import com.hubsi.authmicroservice.adapters.out.security.UserDetailsImpl;
import com.hubsi.authmicroservice.application.usecases.JwtUseCases;
import com.hubsi.authmicroservice.domain.user.User;
import com.hubsi.authmicroservice.domain.user.UserRepository;
import com.hubsi.authmicroservice.utils.enums.Role;
import com.hubsi.authmicroservice.utils.mappers.UserMapper;
import com.hubsi.authmicroservice.utils.mappers.UserMapperImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUseCases jwtUseCases;

    @InjectMocks
    private UserService userService;

    @Spy
    private UserMapper userMapper = new UserMapperImpl();


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoadUserByUsername_UserFound() {
        String email = "test@example.com";
        User user = new User();
        user.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        UserDetailsImpl userDetails = (UserDetailsImpl) userService.loadUserByUsername(email);

        assertNotNull(userDetails);
        assertEquals(email, userDetails.getUsername());
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        String email = "notfound@example.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(email));
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void testRegisterUser() {
        RegisterRequest request = new RegisterRequest(
                "joao@teste.com",
                "senha123",
                "joao@teste.com",
                "senha123"
        );
        String senhaCriptografada = "$2a$10$senhaCriptografada";
        UUID userId = UUID.randomUUID();
        User userSalvo = new User(userId, "João", "Silva", "joao@teste.com", senhaCriptografada, Role.USER);
        String token = "jwtToken";
        String mensagem = "Usuário cadastrado com sucesso!";

        when(userRepository.save(any(User.class))).thenReturn(userSalvo);
        when(jwtUseCases.generateToken(any(UserDetailsImpl.class))).thenReturn(token);

        doReturn(new RegisterResponse(mensagem, token, userSalvo.getEmail(), userSalvo.getNome(), userSalvo.getSobrenome(), userSalvo.getRole(), userSalvo.getId()))
                .when(userMapper).entityToDtoRegister(any(), any(), any());

        RegisterResponse response = userService.registerUser(request);

        assertNotNull(response);
        assertEquals(token, response.token());
        assertEquals(mensagem, response.message());
        assertEquals(userSalvo.getEmail(), response.email());
    }

    @Test
    void testFindByEmail_UserNotFound() {
        String email = "naoexiste@teste.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        Optional<User> result = userService.findByEmail(email);

        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findByEmail(email);
    }
}
