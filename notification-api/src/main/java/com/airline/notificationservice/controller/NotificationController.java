package com.airline.notificationservice.controller;

import com.airline.notificationservice.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notification")
public class NotificationController {

    private final NotificationService service;

    public NotificationController(NotificationService service) {
        this.service = service;
    }

    @PostMapping("/test")
    public ResponseEntity<Void> sendTestNotification() {
        service.logEmailSent("test@example.com", "Hello", "This is a test", true);
        return ResponseEntity.ok().build();
    }
}
