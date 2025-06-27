package com.airline.authservice.security;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.airline.authservice.model.LoginForm;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import static com.airline.authservice.security.SecurityConstants.*;
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    @Value("${secure.key.token:}")
    private String SECRET_KEY;
     public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
         this.setFilterProcessesUrl("/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
            throws AuthenticationException {
            // Process the login request
            try {
                LoginForm user = this.getCredentials(req);
                System.out.println("Attempting to log in: " + user.getEmail());
                return this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                        user.getEmail(),
                        user.getPassword()
                ));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain,
                                            Authentication auth) throws IOException {

        String email = auth.getName();
        StringBuilder roles= new StringBuilder();
        for(GrantedAuthority g: (auth.getAuthorities()))
        {
            roles.append(g.getAuthority());
        }
        System.out.println("Uspesan login, vas role: "+roles);

        SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

        // Create JWT token with subject, roles claim, and expiration
        String token = Jwts.builder()
                .setSubject(email)
                .claim("roles", roles.toString())   // roles is your List or Set of roles
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        res.setContentType("application/json");
        res.setStatus(HttpServletResponse.SC_OK);
        res.getWriter().write("{\"token\":\"" + token + "\"}");
    }

    private LoginForm getCredentials(HttpServletRequest request) {
        // Map dto value.
        LoginForm auth = null;
        try {
            auth = new ObjectMapper().readValue(request.getInputStream(), LoginForm.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return auth;
    }

    private SecretKey getSigningKey() {
        return new SecretKeySpec(SECRET_KEY.getBytes(), "HmacSHA512");
    }
}