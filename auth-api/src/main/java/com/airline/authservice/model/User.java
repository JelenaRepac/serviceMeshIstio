package com.airline.authservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Setter
@Builder
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "airline_user", schema = "airline_users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String firstname;

    @Column(nullable = false)
    private String lastname;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String passportNumber;

    @Column
    private String country;

    @Column
    private Date birthday;

    @Column
    private String phoneNumber;

    @Column
    private String rank;

    @Column
    private boolean isTwoFactorEnabled;
    @Column
    private String secretKey;

    @Column
    private int miles;

    private boolean isEnabled;

    private String passportId;

}