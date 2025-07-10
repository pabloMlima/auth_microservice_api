package com.hubsi.authmicroservice.infrastructure.config;

import com.hubsi.authmicroservice.adapters.out.response.ErrorResponse;
import com.hubsi.authmicroservice.infrastructure.exceptions.EmailSendingException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GlobalHandlerExceptionsTest {

    private WebRequest getWebRequest() {
        return new ServletWebRequest(new MockHttpServletRequest());
    }

    @Test
    void testHandleRuntimeException() {
        GlobalHandlerExceptions handler = new GlobalHandlerExceptions();
        RuntimeException ex = new RuntimeException("Erro inesperado");
        ResponseEntity<ErrorResponse> response = handler.handleRuntimeException(ex, getWebRequest());

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Erro inesperado", response.getBody().message());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getBody().status());
    }

    @Test
    void testHandleUsernameNotFoundException() {
        GlobalHandlerExceptions handler = new GlobalHandlerExceptions();
        UsernameNotFoundException ex = new UsernameNotFoundException("Usuário não encontrado");
        ResponseEntity<ErrorResponse> response = handler.handleUsernameNotFoundException(ex, getWebRequest());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Usuário não encontrado", response.getBody().message());
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getBody().status());
    }

    @Test
    void testHandleBadCredentialsException() {
        GlobalHandlerExceptions handler = new GlobalHandlerExceptions();
        BadCredentialsException ex = new BadCredentialsException("Credenciais inválidas");
        ResponseEntity<ErrorResponse> response = handler.handleBadCredentialsException(ex, getWebRequest());

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Invalid email or password", response.getBody().message());
        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getBody().status());
    }

    @Test
    void testHandleValidationExceptions() {
        GlobalHandlerExceptions handler = new GlobalHandlerExceptions();
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "object");
        bindingResult.addError(new FieldError("object", "email", "Email inválido"));
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);

        ResponseEntity<Object> response = handler.handleValidationExceptions(ex, getWebRequest());

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        assertEquals("One or more fields have validation errors.", errorResponse.message());
        assertEquals(HttpStatus.BAD_REQUEST.value(), errorResponse.status());
        assertTrue(errorResponse.validationErrors() instanceof Map);
        assertTrue(((Map<?, ?>) errorResponse.validationErrors()).containsKey("email"));
    }

    @Test
    void testHandleEmailSendingException() {
        GlobalHandlerExceptions handler = new GlobalHandlerExceptions();
        EmailSendingException ex = new EmailSendingException("Falha ao enviar email", new RuntimeException("Erro de envio"));
        ResponseEntity<ErrorResponse> response = handler.handleEmailSendingException(ex, getWebRequest());

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Falha ao enviar email", response.getBody().message());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getBody().status());
    }

    @Test
    void testHandleMissingServletRequestParameterException() {
        GlobalHandlerExceptions handler = new GlobalHandlerExceptions();
        MissingServletRequestParameterException ex =
                new MissingServletRequestParameterException("email", "String");
        ResponseEntity<ErrorResponse> response = handler.handleMissingServletRequestParameterException(ex, getWebRequest());

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().message().contains("email"));
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getBody().status());
    }
}
