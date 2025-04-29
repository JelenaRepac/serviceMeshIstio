package com.airline.authservice.model;

import com.auth0.jwt.JWT;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

import static com.airline.authservice.security.SecurityConstants.SECRET;
import static com.airline.authservice.security.SecurityConstants.TOKEN_EXPIRATION_TIME;
import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

@Data
@Entity
@NoArgsConstructor
@Table(name = "confirmation_token", schema = "airline_users")
public class ConfirmationToken {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    private String confirmationToken;

    private String emailToSet;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "airlane_user", nullable = false)
    private User airlaneUser;

    public ConfirmationToken(User user) {
        this.airlaneUser = user;
        createdDate = new Date();

        confirmationToken = JWT.create()
                .withSubject(user.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION_TIME))
                .sign(HMAC512(SECRET.getBytes()));
    }


}