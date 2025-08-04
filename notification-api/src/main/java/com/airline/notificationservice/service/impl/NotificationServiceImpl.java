package com.airline.notificationservice.service.impl;

import com.airline.notificationservice.model.NotificationLog;
//import com.airline.notificationservice.repository.NotificationLogRepository;
import com.airline.notificationservice.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class NotificationServiceImpl implements NotificationService {
//    private final NotificationLogRepository repository;

//    @Autowired
//    public NotificationServiceImpl(NotificationLogRepository repository) {
//        this.repository = repository;
//    }

    @Override
    public void logEmailSent(String recipient, String subject, String body, boolean success) {
        NotificationLog log = new NotificationLog();
        log.setRecipientEmail(recipient);
        log.setSubject(subject);
        log.setMessageBody(body);
        log.setSentTime(LocalDateTime.now());
        log.setStatus(success ? "SENT" : "FAILED");

//        repository.save(log);
    }
}
