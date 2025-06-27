package com.airline.authservice.service;

import com.airline.authservice.dto.TokenAuthorizationDto;
import com.airline.authservice.dto.TokenDto;
import com.airline.authservice.model.Country;
import com.airline.authservice.model.User;
import com.google.zxing.WriterException;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface UserService {

    TokenAuthorizationDto save(User user) throws MailjetSocketTimeoutException, MailjetException, IOException, WriterException;
    void verifyTotpCode(String secretKey, String code, String authHeader);
    TokenDto checkAndGenerateTokens(TokenAuthorizationDto tokenAuthorizationDto, String uid);
    ResponseEntity confirm(String confirmationToken);
    User update(User user,String token) throws MailjetSocketTimeoutException, MailjetException;
    User findUserById(Long id);

    User  findUserByEmail(String email);
    List<User> findAll();

    List<Country >getCountry();

}
