package com.myproject.snipurl.models;

import jakarta.persistence.*;

import lombok.Data;

import java.time.LocalDateTime;


@Entity
@Data
public class SnipUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private  String username;

    private String password;

    private LocalDateTime createdAt;

    private String role = "user";

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
