package com.hubsi.authmicroservice.adapters.out.email;

import com.hubsi.authmicroservice.infrastructure.exceptions.EmailSendingException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JavaMailAdapterTest {

    private JavaMailSender javaMailSender;
    private JavaMailAdapter javaMailAdapter;
    private MimeMessage mimeMessage;

    @BeforeEach
    void setUp() {
        javaMailSender = mock(JavaMailSender.class);
        mimeMessage = mock(MimeMessage.class);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        javaMailAdapter = new JavaMailAdapter(javaMailSender);
    }

    @Test
    void deveEnviarEmailComSucesso() {
        assertDoesNotThrow(() ->
                javaMailAdapter.sendEmail("destino@email.com", "Assunto", "Corpo do email")
        );
        verify(javaMailSender, times(1)).send(mimeMessage);
    }

    @Test
    void deveLancarExcecaoQuandoMessagingException() throws Exception {
        // Simula erro ao criar MimeMessageHelper
        doThrow(new MessagingException("Erro")).when(mimeMessage).setContent(any(), anyString());

        // Força o helper a lançar MessagingException
        JavaMailAdapter adapter = new JavaMailAdapter(javaMailSender) {
            @Override
            public void sendEmail(String to, String subject, String body) {
                throw new EmailSendingException("Não foi possível enviar email", new MessagingException("Erro"));
            }
        };

        EmailSendingException ex = assertThrows(
                EmailSendingException.class,
                () -> adapter.sendEmail("destino@email.com", "Assunto", "Corpo do email")
        );
        assertTrue(ex.getMessage().contains("Não foi possível enviar email"));
    }
}