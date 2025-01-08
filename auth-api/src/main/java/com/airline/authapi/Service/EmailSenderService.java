package com.airline.authapi.Service;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import org.springframework.mail.SimpleMailMessage;
public interface EmailSenderService {

     void sendConfirmationEmail(String recipientEmail, String token) throws MailjetException, MailjetSocketTimeoutException;

    }
