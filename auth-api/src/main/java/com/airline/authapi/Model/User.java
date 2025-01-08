package com.airline.authapi.Model;

import lombok.*;

import javax.persistence.*;

@Entity
@Setter
@Builder
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class User {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userid;

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