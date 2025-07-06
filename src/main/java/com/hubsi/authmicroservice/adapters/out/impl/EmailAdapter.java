package com.hubsi.authmicroservice.adapters.out.impl;

public interface EmailAdapter {

    void sendEmail(String to, String subject, String body);
}
