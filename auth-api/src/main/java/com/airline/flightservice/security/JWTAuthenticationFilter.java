package com.airline.flightservice.security;


import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.airline.flightservice.model.LoginForm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import static com.airline.flightservice.security.SecurityConstants.*;
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
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
                                            Authentication auth) {

        String email = auth.getName();
        String roles="";
        for(GrantedAuthority g: (auth.getAuthorities()))
        {
            roles+=g.getAuthority();
        }
        System.out.println("Uspesan login, vas role: "+roles);

        String token = JWT.create().withSubject(email)
                .withClaim("roles", roles)
                .withExpiresAt(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION_TIME))
                .sign(HMAC512(SECRET.getBytes()));

        res.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
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
}