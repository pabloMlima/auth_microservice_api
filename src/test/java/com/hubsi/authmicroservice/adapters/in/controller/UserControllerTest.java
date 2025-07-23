package com.hubsi.authmicroservice.adapters.in.controller;

import com.hubsi.authmicroservice.adapters.in.request.RegisterRequest;
import com.hubsi.authmicroservice.adapters.in.request.ResetPasswordRequest;
import com.hubsi.authmicroservice.adapters.out.response.RegisterResponse;
import com.hubsi.authmicroservice.application.usecases.ResetPasswordUseCases;
import com.hubsi.authmicroservice.application.usecases.UserUseCases;
import com.hubsi.authmicroservice.utils.enums.Role;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserUseCases userUseCases;

    @InjectMocks
    private UserController userController;

    @Test
    void testRegister() {
        // Arrange
        RegisterRequest request = new RegisterRequest("Teste nome", "Teste sobrenome" ,"test@example.com", "password123");
        RegisterResponse expectedResponse = new RegisterResponse(
                "Usuário cadastrado com sucesso!",
                "jwtToken",
                request.email(),
                request.nome(),
                request.sobrenome(),
                Role.USER,
                null
        );
        when(userUseCases.registerUser(request)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<RegisterResponse> response = userController.register(request);

        // Assert
        assertEquals(ResponseEntity.ok(expectedResponse), response);
        verify(userUseCases, times(1)).registerUser(request);
    }

    @Test
    void testRegisterWithDuplicateEmail(){

        RegisterRequest request = new RegisterRequest("Teste nome", "Teste sobrenome" ,"test@example.com", "password123");
        when(userUseCases.registerUser(request)).thenThrow(new IllegalArgumentException("Email já está em uso"));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userController.register(request);
        });

        assertEquals("Email já está em uso", exception.getMessage());
        verify(userUseCases, times(1)).registerUser(request);

    }

    @Test
    void testRegisterWithMissingFields() {
        // Arrange
        RegisterRequest request = new RegisterRequest("", "", "test@example.com", "");
        when(userUseCases.registerUser(request)).thenThrow(new ConstraintViolationException("Os campos obrigatórios não foram preenchidos.", null));

        // Act & Assert
        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {
            userController.register(request);
        });

        assertEquals("Os campos obrigatórios não foram preenchidos.", exception.getMessage());
        verify(userUseCases, times(1)).registerUser(request);
    }

    @Test
    void testRegisterWithWeakPassword() {
        // Arrange
        RegisterRequest request = new RegisterRequest("Teste nome", "Teste sobrenome", "test@example.com", "123");
        when(userUseCases.registerUser(request)).thenThrow(new ConstraintViolationException("A senha deve ter pelo menos 8 caracteres.", null));

        // Act & Assert
        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {
            userController.register(request);
        });

        assertEquals("A senha deve ter pelo menos 8 caracteres.", exception.getMessage());
        verify(userUseCases, times(1)).registerUser(request);
    }

    @Test
    void testRegisterWithNullFields() {
        // Arrange
        RegisterRequest request = new RegisterRequest(null, null, null, null);
        when(userUseCases.registerUser(request)).thenThrow(new ConstraintViolationException("Os campos obrigatórios não foram preenchidos.", null));

        // Act & Assert
        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {
            userController.register(request);
        });

        assertEquals("Os campos obrigatórios não foram preenchidos.", exception.getMessage());
        verify(userUseCases, times(1)).registerUser(request);
    }

    @Test
    void testRegisterWithDuplicateEmailException() {
        // Arrange
        RegisterRequest request = new RegisterRequest("Teste nome", "Teste sobrenome", "test@example.com", "password123");
        when(userUseCases.registerUser(request)).thenThrow(new IllegalStateException("Email já cadastrado."));

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            userController.register(request);
        });

        assertEquals("Email já cadastrado.", exception.getMessage());
        verify(userUseCases, times(1)).registerUser(request);
    }

    @Test
    void testResetPassword() {
        // Arrange
        ResetPasswordRequest request = new ResetPasswordRequest("test@example.com");
        ResetPasswordUseCases resetPasswordUseCases = mock(ResetPasswordUseCases.class);
        UserController controller = new UserController(userUseCases, resetPasswordUseCases);

        // Act
        ResponseEntity<Void> response = controller.resetPassword(request);

        // Assert
        assertEquals(ResponseEntity.noContent().build(), response);
        verify(resetPasswordUseCases, times(1)).resetPassword("test@example.com");
    }

    @Test
    void testUpdatePasswordSuccess() {
        // Arrange
        String token = "validToken";
        String newPassword = "newStrongPassword";
        ResetPasswordUseCases resetPasswordUseCases = mock(ResetPasswordUseCases.class);
        UserController controller = new UserController(userUseCases, resetPasswordUseCases);

        // Act
        ResponseEntity<Void> response = controller.updatePassword(token, newPassword);

        // Assert
        assertEquals(ResponseEntity.noContent().build(), response);
        verify(resetPasswordUseCases, times(1)).confirmResetPassword(token, newPassword);
    }

    @Test
    void testUpdatePasswordWithInvalidToken() {
        // Arrange
        String token = "invalidToken";
        String newPassword = "newStrongPassword";
        ResetPasswordUseCases resetPasswordUseCases = mock(ResetPasswordUseCases.class);
        UserController controller = new UserController(userUseCases, resetPasswordUseCases);

        doThrow(new IllegalArgumentException("Token inválido ou expirado")).when(resetPasswordUseCases)
                .confirmResetPassword(token, newPassword);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            controller.updatePassword(token, newPassword);
        });

        assertEquals("Token inválido ou expirado", exception.getMessage());
        verify(resetPasswordUseCases, times(1)).confirmResetPassword(token, newPassword);
    }

    @Test
    void testUpdatePasswordWithWeakPassword() {
        // Arrange
        String token = "validToken";
        String newPassword = "123";
        ResetPasswordUseCases resetPasswordUseCases = mock(ResetPasswordUseCases.class);
        UserController controller = new UserController(userUseCases, resetPasswordUseCases);

        doThrow(new jakarta.validation.ConstraintViolationException("A senha deve ter pelo menos 8 caracteres.", null))
                .when(resetPasswordUseCases).confirmResetPassword(token, newPassword);

        // Act & Assert
        jakarta.validation.ConstraintViolationException exception = assertThrows(jakarta.validation.ConstraintViolationException.class, () -> {
            controller.updatePassword(token, newPassword);
        });

        assertEquals("A senha deve ter pelo menos 8 caracteres.", exception.getMessage());
        verify(resetPasswordUseCases, times(1)).confirmResetPassword(token, newPassword);
    }
}
