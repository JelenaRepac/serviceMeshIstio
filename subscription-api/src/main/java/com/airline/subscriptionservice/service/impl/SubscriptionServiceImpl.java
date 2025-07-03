package com.airline.subscriptionservice.service.impl;

import com.airline.subscriptionservice.common.MailType;
import com.airline.subscriptionservice.model.Subscriber;
import com.airline.subscriptionservice.model.SubscriptionStatus;
import com.airline.subscriptionservice.repository.SubscriptionRepository;
import com.airline.subscriptionservice.service.SubscriptionService;
import com.mailjet.client.ClientOptions;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import com.mailjet.client.resource.Emailv31;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.kafka.support.serializer.DelegatingSerializer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class SubscriptionServiceImpl implements SubscriptionService {


    private final SubscriptionRepository subscriberRepository;

    public SubscriptionServiceImpl(SubscriptionRepository subscriberRepository) {
        this.subscriberRepository = subscriberRepository;
    }

    @Override
    @Transactional
    public Subscriber subscribe(String email, String name) {
        Optional<Subscriber> existing = subscriberRepository.findByEmail(email);
        if (existing.isPresent()) {
            Subscriber subscriber = existing.get();
            if (subscriber.getStatus().equals(SubscriptionStatus.UNSUBSCRIBED)) {
                subscriber.setStatus(SubscriptionStatus.ACTIVE.name());
                subscriber.setSubscriptionDate(new Date(System.currentTimeMillis()));
                subscriber.setName(name);
                return subscriberRepository.save(subscriber);
            }
            return subscriber;
        }
        Subscriber newSubscriber = Subscriber.builder().
                email(email).
                name(name).
                subscriptionDate(new Date(System.currentTimeMillis())).
                status(SubscriptionStatus.ACTIVE.name()).build();
        return subscriberRepository.save(newSubscriber);
    }

    @Override
    @Transactional
    public void unsubscribe(String email) {
        subscriberRepository.findByEmail(email).ifPresent(subscriber -> {
            subscriber.setStatus(SubscriptionStatus.UNSUBSCRIBED.name());
            subscriberRepository.save(subscriber);
        });
    }

    @Override
    public Optional<Subscriber> getStatus(String email) {
        return subscriberRepository.findByEmail(email);
    }


    @Override
    public List<Subscriber> getAll(String status) {
        if(status==null)
            return subscriberRepository.findAll();
        else
            return subscriberRepository.findSubscribers(status);
    }
}