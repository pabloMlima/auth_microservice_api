package com.hubsi.authmicroservice.domain.user;

import com.hubsi.authmicroservice.adapters.out.persistence.entities.RefreshTokenEntity;
import com.hubsi.authmicroservice.utils.enums.Role;

import java.util.*;

public class User {

    private UUID id;
    private String nome;
    private String sobrenome;
    private String email;
    private String password;
    private Role role;

    public User() {}

    public User(UUID id, String nome, String sobrenome, String email, String password, Role role) {
        this.id = id;
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public User(String nome, String sobrenome, String email, String password, Role role) {
        this(UUID.randomUUID(), nome, sobrenome, email, password, role);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

}
