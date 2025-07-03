package com.airline.subscriptionservice.controller;

import com.airline.subscriptionservice.model.Subscriber;
import com.airline.subscriptionservice.service.SubscriptionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subscription")
public class SubscriptionController {


    private final SubscriptionService subscriptionService;

    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @PostMapping("/subscribe")
    public ResponseEntity<Subscriber> subscribe(@RequestParam String email, @RequestParam(required = false) String name) {
        Subscriber subscriber = subscriptionService.subscribe(email, name);
        return ResponseEntity.ok(subscriber);
    }

    @PostMapping("/unsubscribe")
    public ResponseEntity<Void> unsubscribe(@RequestParam String email) {
        subscriptionService.unsubscribe(email);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/status")
    public ResponseEntity<Subscriber> getStatus(@RequestParam String email) {
        return subscriptionService.getStatus(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping()
    public ResponseEntity<List<Subscriber>> getAll(@RequestParam(value = "status") String status) {
        return new ResponseEntity<>(subscriptionService.getAll(status), HttpStatus.OK);
    }
}
