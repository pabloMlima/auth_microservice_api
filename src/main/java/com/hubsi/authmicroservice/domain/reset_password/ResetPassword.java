package com.hubsi.authmicroservice.domain.reset_password;

import com.hubsi.authmicroservice.domain.user.User;

import java.time.Instant;
import java.util.UUID;

public class ResetPassword {

    private UUID id;

    private User user;

    private String token;

    private boolean usado;

    private boolean expirado;

    private Instant expirationTime;

    public ResetPassword(){

    }

    public ResetPassword(UUID id, User user, String token, boolean usado, boolean expirado, Instant expirationTime) {
        this.id = id;
        this.user = user;
        this.token = token;
        this.usado = usado;
        this.expirado = expirado;
        this.expirationTime = expirationTime;
    }

    public ResetPassword(User user, String token, boolean usado, boolean expirado, Instant expirationTime) {
        this.user = user;
        this.token = token;
        this.usado = usado;
        this.expirado = expirado;
        this.expirationTime = expirationTime;
    }

    public UUID getId() {
        return id;
    }
    public User getUser() {
        return user;
    }
    public String getToken() {
        return token;
    }
    public boolean isUsado() {
        return usado;
    }
    public boolean isExpirado() {
        return expirado;
    }
    public Instant getExpirationTime() {
        return expirationTime;
    }
    public void setId(UUID id) {
        this.id = id;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public void setUsado(boolean usado) {
        this.usado = usado;
    }
    public void setExpirado(boolean expirado) {
        this.expirado = expirado;
    }
    public void setExpirationTime(Instant expirationTime) {
        this.expirationTime = expirationTime;
    }
}
