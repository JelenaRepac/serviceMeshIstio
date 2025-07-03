package com.airline.subscriptionservice.repository;

import com.airline.subscriptionservice.model.Subscriber;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionRepository extends JpaRepository<Subscriber, Long> {
}
