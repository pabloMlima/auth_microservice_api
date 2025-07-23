package com.hubsi.authmicroservice.application.usecases;

public interface ResetPasswordUseCases {

    void resetPassword(String email);

    void confirmResetPassword(String token, String newPassword);
}
