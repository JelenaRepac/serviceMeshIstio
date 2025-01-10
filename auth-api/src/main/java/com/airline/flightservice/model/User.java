package com.airline.flightservice.model;

import lombok.*;

import javax.persistence.*;

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
    private String rank;

    @Column
    private int miles;

    private boolean isEnabled;

}