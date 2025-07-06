package com.hubsi.authmicroservice.adapters;

public interface EmailAdapter {

    void sendEmail(String to, String subject, String body);
}
