package com.airline.notificationservice.service;

public interface NotificationService {

     void logEmailSent(String recipient, String subject, String body, boolean success);
    }
