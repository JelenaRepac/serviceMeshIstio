package com.airline.authservice.service.impl;

import com.airline.authservice.model.*;
import com.airline.authservice.repository.AdminRepository;
import com.airline.authservice.repository.ConfirmationTokenRepository;
import com.airline.authservice.repository.UserRepository;
import com.airline.authservice.service.EmailSenderService;
import com.airline.authservice.service.UserService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import static  com.airline.authservice.security.SecurityConstants.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final EmailSenderService emailSenderService;
    private final BCryptPasswordEncoder encoder ;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final RestTemplate restTemplate;
    private final AdminRepository adminRepository;

    public UserServiceImpl(UserRepository userRepository, EmailSenderService emailSenderService, BCryptPasswordEncoder encoder,
                           ConfirmationTokenRepository confirmationTokenRepository, RestTemplate restTemplate,
                           AdminRepository adminRepository) {
        this.userRepository = userRepository;
        this.emailSenderService = emailSenderService;
        this.encoder = encoder;
        this.confirmationTokenRepository = confirmationTokenRepository;
        this.restTemplate = restTemplate;
        this.adminRepository = adminRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public User save(User user) throws MailjetSocketTimeoutException, MailjetException {

        Optional<User> existingUser = userRepository.findByEmailIgnoreCase(user.getEmail());

            ConfirmationToken confirmationToken;
            if (existingUser.isPresent()) {
                throw new RuntimeException("User with that email already exists");
            } else {

                String encodedPassword = encoder.encode(user.getPassword());
                user.setPassword(encodedPassword);

                userRepository.save(user);

                confirmationToken = new ConfirmationToken(user);
                confirmationTokenRepository.save(confirmationToken);
                emailSenderService.sendConfirmationEmail(user.getEmail(), confirmationToken.getConfirmationToken());

            }
            System.out.println("User created, left to confirm" + confirmationToken.getConfirmationToken());
            return null;
    }

    @Override
    public ResponseEntity confirm(String confirmationToken) {
        ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);
        if(token != null)
        {
            Optional<User> user = userRepository.findByEmailIgnoreCase(token.getAirlaneUser().getEmail());
            if(user.isPresent()) {
                if (token.getEmailToSet() == null)
                    user.get().setEnabled(true);
                else
                    user.get().setEmail(token.getEmailToSet());
                userRepository.save(user.get());
                System.out.println("Email link confirmed");
            }

            return ResponseEntity.status(HttpStatus.FOUND)
                    .header(HttpHeaders.LOCATION, "http://localhost:4200/confirm")
                    .build();

        }
        else
        {
            System.out.println("Link is invalid or broken");
        }
        return null;
    }

@Override
    public User update(User user, String token) throws MailjetSocketTimeoutException, MailjetException {
        String email = JWT.require(Algorithm.HMAC512(SECRET.getBytes())).build()
                .verify(token.replace(TOKEN_PREFIX, "")).getSubject();
        Optional<User> oldUser = userRepository.findByEmailIgnoreCase(email);

        if(oldUser.isPresent()) {


            // Update user details
            if (user.getFirstname() != null)
                oldUser.get().setFirstname(user.getFirstname());
            if (user.getLastname() != null)
                oldUser.get().setLastname(user.getLastname());
            if (user.getPassportNumber() != null)
                oldUser.get().setPassportNumber(user.getPassportNumber());
            if (user.getBirthday() != null)
                oldUser.get().setBirthday(user.getBirthday());
            if (user.getUsername() != null)
                oldUser.get().setUsername(user.getUsername());
            if (user.getPhoneNumber() != null)
                oldUser.get().setPhoneNumber(user.getPhoneNumber());
            if (user.getCountry() != null)
                oldUser.get().setCountry(user.getCountry());
            if (user.getPassword() != null) {
                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                String encodedPassword = encoder.encode(user.getPassword());
                oldUser.get().setPassword(encodedPassword);
            }

            // If the email is updated, handle confirmation logic
            if (user.getEmail() != null) {
                // Save the user before creating confirmation token
                oldUser.get().setEmail(user.getEmail());
                userRepository.save(oldUser.get());  // Ensure the user is saved before creating a token

                ConfirmationToken confirmationToken = new ConfirmationToken(oldUser.get());  // Create token with the updated user
                confirmationToken.setEmailToSet(user.getEmail());
                confirmationTokenRepository.save(confirmationToken);  // Save the confirmation token

               // emailSenderService.sendConfirmationEmail(user.getEmail(), confirmationToken.getConfirmationToken());
            }

            // Save the updated user
            userRepository.save(oldUser.get());

        }
        return null; // You can return the updated user or some appropriate response
    }
    @Override
    public Optional<User> findUserById(Long id) {
        return Optional.empty();
    }

    @Override
    public User findUserByEmail(String email) {

        Optional<User> user = userRepository.findByEmailIgnoreCase(email);
        if(user.isPresent()){
            return user.get();
        }
        throw new RuntimeException("Ne postoji user sa tim email om!");
    }

    @Override
    public List<User> findAll() {

        return userRepository.findAll();
    }

    @Override
    public List<Country> getCountry() {
        String url = "http://flight:9090/flight/flight/country?accessKey=a0c2b52e19dd4518239d16ae667b4c22";

        // Deserialize response directly into List<Country>
        List<Country> countries = restTemplate.exchange(
                url, HttpMethod.GET, null, new ParameterizedTypeReference<List<Country>>() {}
        ).getBody();

        if (countries != null) {
            countries.forEach(country -> System.out.println(country.getCountryName()));
        } else {
            System.out.println("No data found in the response");
        }

        return countries != null ? countries : Collections.emptyList();

    }


}
