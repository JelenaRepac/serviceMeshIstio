package com.airline.notificationservice.service;
import com.airline.notificationservice.common.MailType;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;

public interface EmailSenderService {

     void sendConfirmationEmail(String recipientEmail, String token, MailType mailType) throws MailjetException
             , MailjetSocketTimeoutException;

    }
