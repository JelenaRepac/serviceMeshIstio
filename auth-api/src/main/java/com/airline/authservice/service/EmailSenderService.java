package com.airline.authservice.service;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;

public interface EmailSenderService {

     void sendConfirmationEmail(String recipientEmail, String token) throws MailjetException, MailjetSocketTimeoutException;

    }
