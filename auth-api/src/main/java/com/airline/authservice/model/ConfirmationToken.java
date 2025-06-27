package com.airline.authservice.model;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.*;

import javax.crypto.SecretKey;
import jakarta.persistence.*;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import static com.airline.authservice.security.SecurityConstants.*;

@Data
@Entity
@NoArgsConstructor
@Table(name = "confirmation_token", schema = "airline_users")
public class ConfirmationToken {

    private static final String BEARER_TOKEN_TYPE = "Bearer ";


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    private String confirmationToken;

    private String refreshToken;


    private String emailToSet;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "airlane_user", nullable = false)
    private User airlaneUser;

    public ConfirmationToken(User user) {
        this.airlaneUser = user;
        createdDate = new Date();

        SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

        // Create JWT token
         this.confirmationToken = BEARER_TOKEN_TYPE + Jwts.builder()
                .setSubject(user.getEmail())
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        this.refreshToken =BEARER_TOKEN_TYPE + Jwts.builder()
                .setSubject(user.getEmail())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }


}