package com.hubsi.authmicroservice.adapters.out;

public interface EmailAdapter {

    void sendEmail(String to, String subject, String body);
}
