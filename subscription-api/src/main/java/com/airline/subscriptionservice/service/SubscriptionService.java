package com.airline.subscriptionservice.service;
import com.airline.subscriptionservice.model.Subscriber;

import java.util.List;
import java.util.Optional;

public interface SubscriptionService {

    Subscriber subscribe(String email, String name);
    void unsubscribe(String email);
    Optional<Subscriber> getStatus(String email);


    List<Subscriber> getAll(String status);
}
