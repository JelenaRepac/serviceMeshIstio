package com.airline.flightservice.security;

import com.airline.flightservice.repository.AdminRepository;
import com.airline.flightservice.repository.UserRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import static  com.airline.flightservice.security.SecurityConstants.*;

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
        chain.doFilter(req, res);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request, String token) {

        if (token != null) {
            // parsiranje tokena
            DecodedJWT jwt = JWT.require(Algorithm.HMAC512(SECRET.getBytes())).build()
                    .verify(token.replace(TOKEN_PREFIX, ""));

            // subject je email od korisnika i spakovan je u JWT
            String email = jwt.getSubject();

            // Provera da li se nalazi user u bazi
            if (email != null) {
                List<SimpleGrantedAuthority> role = new ArrayList<>();
                if(adminRepository.existsByUsername(email))
                {
                    role.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
                    return new UsernamePasswordAuthenticationToken(email, null, role);
                }

                if (userRepo.existsByEmail(email))
                {
                    role.add(new SimpleGrantedAuthority("ROLE_USER"));
                    return new UsernamePasswordAuthenticationToken(email, null, role);
                }
                else{
                    role.add(new SimpleGrantedAuthority("ROLE_USER"));
                    return new UsernamePasswordAuthenticationToken(email, null, role);
                }


            }
            return null;
        }
        return null;
    }

}