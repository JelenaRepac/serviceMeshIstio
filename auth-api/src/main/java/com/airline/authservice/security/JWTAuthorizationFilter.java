package com.airline.authservice.security;

import com.airline.authservice.repository.AdminRepository;
import com.airline.authservice.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import static  com.airline.authservice.security.SecurityConstants.*;

//Authorization - checking if the token is valid and if user exists
public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    private final UserRepository userRepo;
    private final AdminRepository adminRepository;


    public JWTAuthorizationFilter(AuthenticationManager authManager, UserRepository userRepo, AdminRepository adminRepository) {
        super(authManager);
        this.userRepo = userRepo;
        this.adminRepository = adminRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        String token = req.getHeader(HEADER_STRING);

        if (token != null) {
            System.out.println("JWT token received: " + token);
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(req, token);

        if (authentication != null) {
            System.out.println("Authentication successful for user: " + authentication.getName());
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        try {
            chain.doFilter(req, res);
        } catch (Exception e) {
            e.printStackTrace(); // Or log it properly
            res.setStatus(HttpServletResponse.SC_FORBIDDEN); // Optional: override status for debugging
            res.getWriter().write("Access denied: " + e.getMessage());
        }

    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request, String token) {
        if (token == null || !token.startsWith(TOKEN_PREFIX)) {
            return null;
        }


        try {
            // Parsiranje i validacija JWT tokena
            Claims claims = Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8)))
                    .build()
                    .parseSignedClaims(token.replace(TOKEN_PREFIX, ""))
                    .getPayload();


            String email = claims.getSubject();

            if (email == null) {
                return null;
            }

            List<SimpleGrantedAuthority> authorities = new ArrayList<>();

            if (adminRepository.existsByUsername(email)) {
                authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            } else if (userRepo.existsByEmail(email)) {
                authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            } else {
                // Ako korisnik nije pronađen ni kao admin ni kao user
                return null;
            }

            return new UsernamePasswordAuthenticationToken(email, null, authorities);
        } catch (JwtException | IllegalArgumentException e) {
            // Token nije validan ili je istekao
            return null;
        }
    }


}