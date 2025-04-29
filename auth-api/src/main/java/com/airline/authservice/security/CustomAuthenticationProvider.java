package com.airline.authservice.security;

import com.airline.authservice.model.Admin;
import com.airline.authservice.model.User;
import com.airline.authservice.repository.AdminRepository;
import com.airline.authservice.repository.UserRepository;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Setter
    private PasswordEncoder encoder;
    private final UserRepository userRepo;
    private final AdminRepository adminRepository;

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
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        // First check if the user is an admin
        Admin admin = adminRepository.findAdminByUsername(email);
        if (admin != null) {
            if (encoder.matches(password, admin.getPassword())) {
                authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
                return new UsernamePasswordAuthenticationToken(email, password, authorities);
            } else {
                throw new BadCredentialsException("Incorrect password. Please try again!");
            }
        }

        // If not admin, check if the user exists
        Optional<User> userOptional = userRepo.findByEmailIgnoreCase(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (encoder.matches(password, user.getPassword())) {
                authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
                return new UsernamePasswordAuthenticationToken(email, password, authorities);
            } else {
                throw new BadCredentialsException("Incorrect password. Please try again!");
            }
        }

        // If no user or admin is found
        throw new UsernameNotFoundException("No user found with email: " + email);
    }


    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(UsernamePasswordAuthenticationToken.class);
    }

}