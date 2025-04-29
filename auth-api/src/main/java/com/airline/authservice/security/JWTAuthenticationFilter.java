package com.airline.authservice.security;


import java.io.IOException;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.airline.authservice.model.LoginForm;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
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

        String token = JWT.create().withSubject(email)
                .withClaim("roles", roles.toString())
                .withExpiresAt(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION_TIME))
                .sign(Algorithm.HMAC256(SECRET.getBytes()));

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