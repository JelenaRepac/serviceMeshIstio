package com.airline.authservice.controller;

import com.airline.authservice.model.Admin;
import com.airline.authservice.model.User;
import com.airline.authservice.service.AdminService;
import com.airline.authservice.service.UserService;
import com.airline.authservice.service.impl.UserServiceImpl;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.airline.authservice.security.SecurityConstants.*;


@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final AdminService adminService;


    public AuthController(UserServiceImpl userService, AdminService adminService) {
        this.userService = userService;
        this.adminService = adminService;
    }

    //REGISTRATION OF USERS WITH EMAIL SENDING CONFIRMATION
    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public User saveUser(@RequestBody User user) throws MailjetSocketTimeoutException, MailjetException {
        return userService.save(user);
    }


    //only admin can see and manage other users
    //@PreAuthorize("hasAuthority('ROLE_ADMIN')")
//    @PreAuthorize("@securityService.isUserGold('GOLD')")
    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<User> getUsers() {
        return userService.findAll();
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping(value = "/admin",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Admin> getAdmins() {
        return adminService.getAll();
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

    @PostMapping(value = "/admin",consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addAdmin(@RequestBody User user) throws MailjetSocketTimeoutException, MailjetException {
        adminService.addAdmin(user);
        return new ResponseEntity(HttpStatus.CREATED);
    }
    @DeleteMapping(value = "/admin/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity deleteAdmin(@PathVariable("id") Integer id) throws MailjetSocketTimeoutException, MailjetException {
        adminService.deleteAdmin(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    //GET USER PROFILE
    @GetMapping(value = "/profile", produces = MediaType.APPLICATION_JSON_VALUE)
    public User getUserByEmail(@RequestParam("email") String email) {
        return userService.findUserByEmail(email);
    }


}
