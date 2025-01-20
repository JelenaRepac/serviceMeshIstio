package com.airline.flightservice.security;

import com.airline.flightservice.model.User;
import com.airline.flightservice.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("securityService")
public class CustomSecurityService {

    private final  UserRepository userRepository;

    public CustomSecurityService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean isUserGold(String rank) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        System.out.println(userEmail+"hej");
        Optional<User> user = userRepository.findByEmailIgnoreCase(userEmail);

        return user.filter(value -> rank.equalsIgnoreCase(value.getRank())).isPresent();

    }
}
