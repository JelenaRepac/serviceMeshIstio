package com.airline.authservice.service.impl;

import com.airline.authservice.common.ConvertToJson;
import com.airline.authservice.common.LocaleLanguage;
import com.airline.authservice.common.MailType;
import com.airline.authservice.security.TwoFactorAuthentication;
import com.airline.authservice.dto.TokenAuthorizationDto;
import com.airline.authservice.dto.TokenDto;
import com.airline.authservice.exception.BadRequestCustomException;
import com.airline.authservice.exception.ForbiddenExceptionCustom;
import com.airline.authservice.model.*;
import com.airline.authservice.repository.AdminRepository;
import com.airline.authservice.repository.ConfirmationTokenRepository;
import com.airline.authservice.repository.UserRepository;
import com.airline.authservice.service.RemoteEmailSenderService;
import com.airline.authservice.service.UserService;
import com.google.zxing.WriterException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.airline.authservice.security.SecurityConstants.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final TokenGenerationService tokenGenerationService;
    private final MessageSource messageSource;

    private final RemoteEmailSenderService remoteEmailSenderService;
    private final LocaleLanguage localeLanguage;


    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder encoder,
                           ConfirmationTokenRepository confirmationTokenRepository,
                           TokenGenerationService tokenGenerationService, MessageSource messageSource, RemoteEmailSenderService remoteEmailSenderService, LocaleLanguage localeLanguage) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.confirmationTokenRepository = confirmationTokenRepository;
        this.tokenGenerationService = tokenGenerationService;
        this.messageSource = messageSource;
        this.remoteEmailSenderService = remoteEmailSenderService;
        this.localeLanguage = localeLanguage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TokenAuthorizationDto save(User user) throws IOException, WriterException {
        log.info("REGISTER USER");
        log.info("User {}", ConvertToJson.convertObjectToJsonString(user));

        Optional<User> userByEmail = userRepository.findByEmailIgnoreCase(user.getEmail());
        if (userByEmail.isPresent()) {
            throw new RuntimeException("User with this email already exists");
        }

        Optional<User> userByUsername = userRepository.findByUsername(user.getUsername());
        if (userByUsername.isPresent()) {
            throw new RuntimeException("User with this username already exists");
        }

        ConfirmationToken confirmationToken;


        String encodedPassword = encoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        String secretKey = TwoFactorAuthentication.generateSecretKey();
        user.setSecretKey(secretKey);
        user.setTwoFactorEnabled(true);

        userRepository.save(user);

        confirmationToken = new ConfirmationToken(user);
        confirmationTokenRepository.save(confirmationToken);

        String email = user.getEmail();
        String issuer = "YourAppName";
        String barCodeUrl = TwoFactorAuthentication.getGoogleAuthenticatorBarCode(secretKey, email, issuer);
        TwoFactorAuthentication.createQRCode(barCodeUrl, "./totp-" + user.getId() + ".png", 200, 200); // or return as base64 if needed


        String qrCodeBase64 = TwoFactorAuthentication.getQRCodeBase64(barCodeUrl, 200, 200);

        log.info("User created, waiting for confirmation token: {}", confirmationToken.getConfirmationToken());

        return TokenAuthorizationDto.builder()
                .accessToken(confirmationToken.getConfirmationToken())
                .refreshToken(confirmationToken.getRefreshToken())
                .qrCodeBase64(qrCodeBase64)
                .build();
    }

    @Override
    public void verifyTotpCode(String email, String code, String authHeader) {
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.isTwoFactorEnabled()) {
            throw new BadRequestCustomException("2FA is not enabled for this user.", "400");
        }

        String expectedCode = TwoFactorAuthentication.getTOTPCode(user.getSecretKey());
        System.out.println(expectedCode);
        Boolean value = expectedCode.equals(code);
        if (value) {
            user.setEnabled(true);
            userRepository.save(user);
            //slanje mejla za potvrdu naloga
            remoteEmailSenderService.sendConfirmationEmail(user.getEmail(), authHeader, MailType.ACCOUNT_CONFIRMATION);
        } else {
            throw new BadRequestCustomException("Invalid TOTP code.", "400");
        }
    }

    @Transactional
    @Override
    public TokenDto checkAndGenerateTokens(TokenAuthorizationDto tokenAuthorizationDto, String uid) {
        log.info("[{}] Check and generate token", uid);
        log.info("[{}] REQUEST { tokenAuthorizationDto: {} }", uid, ConvertToJson.convertObjectToJsonString(tokenAuthorizationDto));
        // validacija refresh tokena
        checkTokensValidity(tokenAuthorizationDto.getRefreshToken());

        // na osnovu refresh tokena - kreiranje novog access/refresh tokena
        log.info("[{}] Username extraction started.", uid);
        String username = tokenGenerationService.getUsernameFromToken(tokenAuthorizationDto.getRefreshToken().substring(7));
        log.info("[{}] Username extracted successfully. username: {}", uid, username);

        //get iz db

        ConfirmationToken confirmationToken = confirmationTokenRepository.findByRefreshToken(tokenAuthorizationDto.getRefreshToken());
        String accessToken;
        String refreshToken;

        if (confirmationToken != null) {
            accessToken = tokenGenerationService.generateAccessToken(username);
            refreshToken = tokenGenerationService.generateRefreshToken(username);

            ZonedDateTime zonedDateTime = ZonedDateTime.now();
            Date date = Date.from(zonedDateTime.toInstant());

            confirmationToken.setConfirmationToken(accessToken);
            confirmationToken.setRefreshToken(refreshToken);
            confirmationToken.setCreatedDate(date);

        } else {
            log.error("[{}] ERROR {}", uid, messageSource.getMessage("error.message.user_not_exist", null, localeLanguage.getLocale()));
            throw new ForbiddenExceptionCustom(messageSource.getMessage("error.message.user_not_exist", null, localeLanguage.getLocale()),
                    messageSource.getMessage("error.code.user_not_exist", null, localeLanguage.getLocale()));
        }

        confirmationTokenRepository.save(confirmationToken);

        log.info("[{}] Tokens creation successfully done for user: {} ", uid, username);
        return TokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();


    }

    public void checkTokensValidity(String refreshToken) {
        boolean refreshTokenValidity = tokenGenerationService.validateToken(refreshToken.substring(7));
        if (!refreshTokenValidity) {
            throw new ForbiddenExceptionCustom(messageSource.getMessage("error.message.forbidden", null, localeLanguage.getLocale()),
                    messageSource.getMessage("error.code.forbidden", null, localeLanguage.getLocale()));
        }
    }

    @Override
    public ResponseEntity confirm(String confirmationToken) {
        ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);
        if (token != null) {
            Optional<User> user = userRepository.findByEmailIgnoreCase(token.getAirlaneUser().getEmail());
            if (user.isPresent()) {
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

        } else {
            System.out.println("Link is invalid or broken");
        }
        return null;
    }

    @Override
    public User update(User user, String token) {
        String email = Jwts.parser()
                .setSigningKey(Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                .getBody()
                .getSubject();

        Optional<User> oldUser = userRepository.findByEmailIgnoreCase(email);
        User updatedUser = null;
        if (oldUser.isPresent()) {


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
            updatedUser = userRepository.save(oldUser.get());

        }
        return updatedUser; // You can return the updated user or some appropriate response
    }

    @Override
    public User findUserById(Long id) {
        log.info("GET user by id {}", id);

        User user = userRepository.findById(id).get();
        log.info("Successfully returned user by id");
        return user;
    }

    @Override
    public User findUserByEmail(String email) {

        Optional<User> user = userRepository.findByEmailIgnoreCase(email);
        if (user.isPresent()) {
            return user.get();
        }
        throw new RuntimeException("Ne postoji user sa tim email om!");
    }

    @Override
    public List<User> findAll() {

        return userRepository.findAll();
    }




}
