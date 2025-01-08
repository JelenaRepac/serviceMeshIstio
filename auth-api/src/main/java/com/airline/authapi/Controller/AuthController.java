package com.airline.authapi.Controller;

import com.airline.authapi.Model.User;
import com.airline.authapi.Service.UserService;
import com.airline.authapi.Service.impl.UserServiceImpl;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import static com.airline.authapi.security.SecurityConstants.*;


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

    //CONFIRMING ACCOUNT IN EMAIL
    @RequestMapping(value = "/confirm-account", method = {RequestMethod.GET, RequestMethod.POST})
    public void confirmUserAccount(@RequestParam("token") String confirmationToken) {
        userService.confirm(confirmationToken);
    }


    @PostMapping(value="/update",consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public User updateUser(@RequestBody User user,@RequestHeader(value = HEADER_STRING) String token) throws MailjetSocketTimeoutException, MailjetException {
        return userService.update(user,token);
    }

}
