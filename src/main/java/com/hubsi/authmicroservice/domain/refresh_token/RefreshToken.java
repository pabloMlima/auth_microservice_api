package com.hubsi.authmicroservice.domain.refresh_token;

import com.hubsi.authmicroservice.domain.user.User;

import java.time.Instant;
import java.util.UUID;


public class RefreshToken {

    private UUID id;

    private User user;

    private Instant createdAt;

    private Instant expiresAt;

    public RefreshToken() {
    }

    public RefreshToken(UUID id, User user, Instant createdAt, Instant expiresAt) {
        this.id = id;
        this.user = user;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
    }

    public RefreshToken(User user, Instant createdAt, Instant expiresAt) {
        this.user = user;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
    }

    public UUID getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }

}