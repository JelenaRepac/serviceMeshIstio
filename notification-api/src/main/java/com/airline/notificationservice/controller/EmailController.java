package com.airline.notificationservice.controller;

import com.airline.notificationservice.dto.EmailRequest;
import com.airline.notificationservice.service.EmailSenderService;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
public class EmailController {

    private final EmailSenderService emailSenderService;

//    @PostMapping("/send-confirmation")
//    public ResponseEntity<Void> sendConfirmationEmail(@RequestBody EmailRequest emailRequest) throws MailjetSocketTimeoutException, MailjetException {
//        emailSenderService.sendConfirmationEmail(emailRequest.getRecipientEmail(), emailRequest.getToken(), emailRequest.getMailType());
//        return ResponseEntity.ok().build();
//    }
}
