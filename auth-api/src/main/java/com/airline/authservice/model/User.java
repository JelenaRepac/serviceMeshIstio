package com.airline.authservice.model;

import lombok.*;

import javax.persistence.*;
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

    @Column(nullable = false)
    private String country;

    @Column(nullable = false)
    private Date birthday;

    @Column(nullable = false)
    private String phoneNumber;

    @Column
    private String rank;

    @Column
    private int miles;

    private boolean isEnabled;

}