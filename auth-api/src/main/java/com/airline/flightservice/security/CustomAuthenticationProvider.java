package com.airline.flightservice.security;

import com.airline.flightservice.model.Admin;
import com.airline.flightservice.model.User;
import com.airline.flightservice.repository.AdminRepository;
import com.airline.flightservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider{

    private PasswordEncoder encoder;
    private UserRepository userRepo;
    private AdminRepository adminRepository;

    @Autowired
    public CustomAuthenticationProvider(UserRepository userRepo, AdminRepository adminRepository) {
        super();
        this.userRepo = userRepo;
        this.adminRepository = adminRepository;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String password = authentication.getCredentials().toString();
        List<SimpleGrantedAuthority> role = new ArrayList<>();
        if(adminRepository.findAdminByUsername(email)!= null) {
            Admin admin = adminRepository.findAdminByUsername(email);
            System.out.println(admin.getUsername() + " us");
            if (encoder.matches(password, admin.getPassword())) {
                role.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
                return new UsernamePasswordAuthenticationToken(email, password, role);
            }
        }
        else if(userRepo.findByEmailIgnoreCase(email) != null)
        {
            User user = userRepo.findByEmailIgnoreCase(email);

            if (encoder.matches(password, user.getPassword())) {
                role.add(new SimpleGrantedAuthority("ROLE_USER"));
                return new UsernamePasswordAuthenticationToken(email, password, role);
            }
        }else
            throw new BadCredentialsException("Authentication failed");


        throw new BadCredentialsException("Authentication failed");

    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(UsernamePasswordAuthenticationToken.class);
    }

    public void setEncoder(PasswordEncoder encoder) {
        this.encoder = encoder;
    }
}