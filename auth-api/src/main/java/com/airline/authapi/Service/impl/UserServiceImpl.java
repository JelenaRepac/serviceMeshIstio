package com.airline.authapi.Service.impl;

import com.airline.authapi.Model.ConfirmationToken;
import com.airline.authapi.Model.User;
import com.airline.authapi.Repository.ConfirmationTokenRepository;
import com.airline.authapi.Repository.UserRepository;
import com.airline.authapi.Service.EmailSenderService;
import com.airline.authapi.Service.UserService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import static  com.airline.authapi.security.SecurityConstants.*;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final EmailSenderService emailSenderService;
    private final ConfirmationTokenRepository confirmationTokenRepository;

    public UserServiceImpl(UserRepository userRepository, EmailSenderService emailSenderService,
                           ConfirmationTokenRepository confirmationTokenRepository) {
        this.userRepository = userRepository;
        this.emailSenderService = emailSenderService;
        this.confirmationTokenRepository = confirmationTokenRepository;
    }

    @Override
    public User save(User user) throws MailjetSocketTimeoutException, MailjetException {

        User existingUser = userRepository.findByEmailIgnoreCase(user.getEmail());

        ConfirmationToken confirmationToken;
        if (existingUser != null) {
            System.out.println("User with that email already exists");
            return null;
        } else {

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
            User user = userRepository.findByEmailIgnoreCase(token.getUser().getEmail());
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
    public User update(User user,String token) throws MailjetSocketTimeoutException, MailjetException {

        String email = JWT.require(Algorithm.HMAC512(SECRET.getBytes())).build()
                .verify(token.replace(TOKEN_PREFIX, "")).getSubject();
        User oldUser = userRepository.findByEmailIgnoreCase(email);
        if(user.getFirstname()!=null)
            oldUser.setFirstname(user.getFirstname());
        if(user.getLastname()!=null)
            oldUser.setLastname(user.getLastname());
        if(user.getPassportNumber()!=null)
            oldUser.setPassportNumber(user.getPassportNumber());
        if(user.getPassword()!=null)
        {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String encodedPassword = encoder.encode(user.getPassword());
            oldUser.setPassword(encodedPassword);
        }
        if(user.getEmail()!=null)
        {
            // oldUser.setEnabled(false);
            // oldUser.setEmail(user.getEmail());
            ConfirmationToken confirmationToken = new ConfirmationToken(oldUser);
            confirmationToken.setEmailToSet(user.getEmail());
            confirmationTokenRepository.save(confirmationToken);

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(user.getEmail());
            mailMessage.setSubject("Complete Registration!");
            mailMessage.setFrom("apetrovic8517rn@raf.rs");
            mailMessage.setText("To confirm your account, please click here : "
                    +"http://localhost:8080/confirm-account?token="+confirmationToken.getConfirmationToken());

            confirmationToken = new ConfirmationToken(user);
            confirmationTokenRepository.save(confirmationToken);
            emailSenderService.sendConfirmationEmail(user.getEmail(), confirmationToken.getConfirmationToken());
        }

        userRepository.save(oldUser);




        return null;
    }
    @Override
    public Optional<User> findUserById(Long id) {
        return Optional.empty();
    }
}
