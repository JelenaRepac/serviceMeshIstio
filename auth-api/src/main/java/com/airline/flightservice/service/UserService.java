package com.airline.flightservice.service;

import com.airline.flightservice.model.Country;
import com.airline.flightservice.model.User;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User save(User user) throws MailjetSocketTimeoutException, MailjetException;
    ResponseEntity confirm(String confirmationToken);
    User update(User user,String token) throws MailjetSocketTimeoutException, MailjetException;
    Optional<User> findUserById(Long id);

    User  findUserByEmail(String email);
    List<User> findAll();

    List<Country >getCountry();
}
