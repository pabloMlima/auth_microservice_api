package com.hubsi.authmicroservice.application.port.out;

public interface EmailAdapter {

    void sendEmail(String to, String subject, String body);
}
