package com.hubsi.authmicroservice.infrastructure.port.out;

public interface EmailAdapter {

    /**
     * Envia um e-mail para o destinatário especificado.
     *
     * @param to      O endereço de e-mail do destinatário
     * @param subject O assunto do e-mail
     * @param body    O corpo do e-mail
     */
    void sendEmail(String to, String subject, String body);
}
