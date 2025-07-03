package com.airline.subscriptionservice.repository;

import com.airline.subscriptionservice.model.Subscriber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscriber, Long> {

    Optional<Subscriber> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query(value = """
                SELECT s FROM Subscriber s 
                WHERE s.status = :status
            """)
    List<Subscriber> findSubscribers(String status);
}
