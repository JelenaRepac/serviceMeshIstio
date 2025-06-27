package com.airline.authservice.service.impl;

import com.airline.authservice.common.MailType;
import com.airline.authservice.dto.EmailRequest;
import com.airline.authservice.kafka.EmailEvent;
import com.airline.authservice.kafka.KafkaConfig;
import com.airline.authservice.service.RemoteEmailSenderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

//Servis za pozivanje notification-api-a
@Service
@Slf4j
@RequiredArgsConstructor
public class RemoteEmailSenderServiceImpl implements RemoteEmailSenderService {

    private final RestTemplate restTemplate;

    @Value("${email-service.url}")
    private String emailServiceUrl;
    @Autowired
    private KafkaTemplate<String, EmailEvent> kafkaTemplate;

    private final String emailTopic = "email-send-topic";

    @Override
    public void sendConfirmationEmail(String email, String token, MailType mailType) {

        log.info("SEND CONFIRMATION MAIL");
        log.info("REQUEST: { email:{}, token:{}, type:{}}", email, token, mailType);


        EmailEvent event = new EmailEvent(email, token, mailType);

        // Pošalji event na Kafka topic
        kafkaTemplate.send(emailTopic, event);
        log.info("Kafka message for email sending queued");


//        EmailRequest request = new EmailRequest(email, token, mailType);
//        restTemplate.postForEntity(emailServiceUrl, request, Void.class);

        log.info("Successfully sent mail");

    }
}
