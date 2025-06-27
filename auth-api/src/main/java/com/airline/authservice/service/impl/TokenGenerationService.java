package com.airline.authservice.service.impl;


import com.airline.authservice.exception.InvalidAuthException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class TokenGenerationService {

    private static final String BEARER_TOKEN_TYPE = "Bearer ";
    @Value("${secure.key.token:}")
    private String SECRET_KEY;
    private static final long ACCESS_TOKEN_EXPIRATION = 1000 * 60 * 60; // 1 hour
    private static final long REFRESH_TOKEN_EXPIRATION = 1000 * 60 * 60 * 24; // 1 day


    private SecretKey getSigningKey() {
        return new SecretKeySpec(SECRET_KEY.getBytes(), "HmacSHA512");
    }

    // GENERISANJE ACCESS TOKENA
    public String generateAccessToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return generateToken(username, claims, ACCESS_TOKEN_EXPIRATION);
    }

    // GENERISANJE REFRESH TOKENA
    public String generateRefreshToken(String username) {
        return generateToken(username, new HashMap<>(), REFRESH_TOKEN_EXPIRATION);
    }

    private String generateToken(String username, Map<String, Object> additionalClaims, long expirationMillis) {
        ZonedDateTime zonedDateTime = ZonedDateTime.now();

        Date issuedAt = Date.from(zonedDateTime.toInstant());
        Date expirationDate = Date.from(Instant.now().plusMillis(REFRESH_TOKEN_EXPIRATION));

        Map<String, Object> claims = new HashMap<>(additionalClaims);
        claims.put("sub", username);
        claims.put("iat", issuedAt);
        claims.put("exp", expirationDate);

        return BEARER_TOKEN_TYPE + Jwts.builder()
                .claims(claims)
                .signWith(getSigningKey())
                .compact();
    }

    public Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            throw new InvalidAuthException("Access token has expired");
        } catch (MalformedJwtException e) {
            throw new InvalidAuthException("Invalid access token");
        } catch (SignatureException e) {
            throw new InvalidAuthException("Invalid JWT signature");
        }
    }

    public String getUsernameFromToken(String token) {
        return getClaims(token).getSubject();
    }

    public String getRolesFromToken(String token) {
        try {
            return getClaims(token).get("permissions").toString();
        } catch (ExpiredJwtException e) {
           return e.getClaims().get("permissions").toString();
        }
    }
}

