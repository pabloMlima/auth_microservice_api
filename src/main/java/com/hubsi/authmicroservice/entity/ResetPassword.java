package com.hubsi.authmicroservice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResetPassword {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, updatable = false)
    private String token;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean usado;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean expirado;

    @Column(nullable = false)
    private Instant expirationTime;

}
