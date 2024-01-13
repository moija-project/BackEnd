package com.example.moija_project.entities;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Blob;
import java.sql.Timestamp;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@Table(name = "user")
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "gender", nullable = false)
    private boolean gender = false;

    @Column(name = "birth", nullable = false)
    private LocalDate birth;

    @Column(name = "phone_number", nullable = false)
    private int phoneNumber;

    @Column(name = "name", nullable = false)
    private String name;

    @Lob
    @Column(name = "profile", columnDefinition= "BLOB")
    private Blob profile;

    @Column(name = "time_join", nullable = false)
    private Timestamp timeJoin;

    @Column(name = "reliability_user", nullable = false, columnDefinition = "FLOAT DEFAULT 3")
    private float reliabilityUser;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "is_available", nullable = false, columnDefinition = "BOOLEAN DEFAULT true")
    private boolean isAvailable;

}
