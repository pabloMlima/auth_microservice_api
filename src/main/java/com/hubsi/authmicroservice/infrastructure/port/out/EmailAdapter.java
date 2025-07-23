package com.hubsi.authmicroservice.infrastructure.port.out;

public interface EmailAdapter {

    void sendEmail(String to, String subject, String body);
}
