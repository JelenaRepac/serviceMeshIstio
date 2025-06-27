package com.airline.reservationservice.service.impl;

import com.airline.reservationservice.common.MailType;
import com.airline.reservationservice.kafka.EmailEvent;
import com.airline.reservationservice.service.RemoteEmailSenderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

//Servis za pozivanje notification-api-a
@Service
@RequiredArgsConstructor
@Slf4j
public class RemoteEmailSenderServiceImpl implements RemoteEmailSenderService {

    private final RestTemplate restTemplate;
    @Autowired
    private KafkaTemplate<String, EmailEvent> kafkaTemplate;



    private final String emailTopic = "email-send-topic";

    @Value("${email-service.url}")
    private String emailServiceUrl;

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
