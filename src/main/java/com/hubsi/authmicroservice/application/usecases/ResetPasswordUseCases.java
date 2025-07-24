package com.hubsi.authmicroservice.application.usecases;

public interface ResetPasswordUseCases {

    /**
     * Envia um e-mail para o usuário com um link para redefinir a senha.
     *
     * @param email o e-mail do usuário
     */
    void resetPassword(String email);

    /**
     * Confirma a redefinição da senha usando o token e a nova senha fornecidos.
     *
     * @param token o token de redefinição de senha
     * @param newPassword a nova senha do usuário
     */
    void confirmResetPassword(String token, String newPassword);
}
