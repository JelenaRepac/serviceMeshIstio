package com.airline.flightservice.service.impl;

import com.airline.flightservice.model.ConfirmationToken;
import com.airline.flightservice.model.User;
import com.airline.flightservice.repository.ConfirmationTokenRepository;
import com.airline.flightservice.repository.UserRepository;
import com.airline.flightservice.service.EmailSenderService;
import com.airline.flightservice.service.UserService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static  com.airline.flightservice.security.SecurityConstants.*;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final EmailSenderService emailSenderService;
    private final BCryptPasswordEncoder encoder ;
    private final ConfirmationTokenRepository confirmationTokenRepository;

    public UserServiceImpl(UserRepository userRepository, EmailSenderService emailSenderService, BCryptPasswordEncoder encoder,
                           ConfirmationTokenRepository confirmationTokenRepository) {
        this.userRepository = userRepository;
        this.emailSenderService = emailSenderService;
        this.encoder = encoder;
        this.confirmationTokenRepository = confirmationTokenRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public User save(User user) throws MailjetSocketTimeoutException, MailjetException {

        User existingUser = userRepository.findByEmailIgnoreCase(user.getEmail());

        ConfirmationToken confirmationToken;
        if (existingUser != null) {
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
    public User confirm(String confirmationToken) {
        ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);
        if(token != null)
        {
            User user = userRepository.findByEmailIgnoreCase(token.getAirlaneUser().getEmail());
            if(token.getEmailToSet()==null)
                user.setEnabled(true);
            else
                user.setEmail(token.getEmailToSet());
            userRepository.save(user);
            System.out.println("Email link confirmed");
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
        User oldUser = userRepository.findByEmailIgnoreCase(email);

        // Update user details
        if (user.getFirstname() != null)
            oldUser.setFirstname(user.getFirstname());
        if (user.getLastname() != null)
            oldUser.setLastname(user.getLastname());
        if (user.getPassportNumber() != null)
            oldUser.setPassportNumber(user.getPassportNumber());
        if (user.getPassword() != null) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String encodedPassword = encoder.encode(user.getPassword());
            oldUser.setPassword(encodedPassword);
        }

        // If the email is updated, handle confirmation logic
        if (user.getEmail() != null) {
            // Save the user before creating confirmation token
            oldUser.setEmail(user.getEmail());
            userRepository.save(oldUser);  // Ensure the user is saved before creating a token

            ConfirmationToken confirmationToken = new ConfirmationToken(oldUser);  // Create token with the updated user
            confirmationToken.setEmailToSet(user.getEmail());
            confirmationTokenRepository.save(confirmationToken);  // Save the confirmation token

            emailSenderService.sendConfirmationEmail(user.getEmail(), confirmationToken.getConfirmationToken());
        }

        // Save the updated user
        userRepository.save(oldUser);

        return null; // You can return the updated user or some appropriate response
    }
    @Override
    public Optional<User> findUserById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }
}
