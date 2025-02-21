package com.airline.flightservice.controller;

import com.airline.flightservice.model.Country;
import com.airline.flightservice.model.User;
import com.airline.flightservice.service.UserService;
import com.airline.flightservice.service.impl.UserServiceImpl;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.airline.flightservice.security.SecurityConstants.*;


@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserServiceImpl userService) {
        this.userService = userService;
    }

    //REGISTRATION OF USERS WITH EMAIL SENDING CONFIRMATION
    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public User saveUser(@RequestBody User user) throws MailjetSocketTimeoutException, MailjetException {
        return userService.save(user);
    }


    //only admin can see and manage other users
    //@PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PreAuthorize("@securityService.isUserGold('GOLD')")
    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<User> getUsers() {
        return userService.findAll();
    }


    //CONFIRMING ACCOUNT IN EMAIL
    @RequestMapping(value = "/confirm-account", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<Void> confirmUserAccount(@RequestParam("token") String confirmationToken) {
        return userService.confirm(confirmationToken);
    }

    //UPDATE USER INFO
    @PostMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public User updateUser(@RequestBody User user, @RequestHeader(value = HEADER_STRING) String token) throws MailjetSocketTimeoutException, MailjetException {
        return userService.update(user, token);
    }

    //GET USER INFO
    @GetMapping(value = "/profile",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public User  getUser(@RequestParam("email") String email) {
        return userService.findUserByEmail(email);
    }

    @GetMapping(value = "/country",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Country>  getCountry() {
        return userService.getCountry();
    }

}
