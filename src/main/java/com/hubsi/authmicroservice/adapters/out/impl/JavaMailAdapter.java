package com.hubsi.authmicroservice.adapters.out.impl;

import com.hubsi.authmicroservice.adapters.out.EmailAdapter;
import com.hubsi.authmicroservice.infrastructure.exceptions.EmailSendingException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JavaMailAdapter implements EmailAdapter {

    private final JavaMailSender javaMailSender;

    @Override
    public void sendEmail(String to, String subject, String body) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new EmailSendingException("Não foi possível enviar email", e);
        }
    }
}
