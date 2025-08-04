package com.airline.authservice.service.impl;

import com.airline.authservice.common.MailType;
import com.airline.authservice.kafka.EmailEvent;
import com.airline.authservice.service.RemoteEmailSenderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

//Servis za pozivanje notification-api-a
@Service
@Slf4j
@RequiredArgsConstructor
public class RemoteEmailSenderServiceImpl implements RemoteEmailSenderService {




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


        log.info("Successfully sent mail");

    }
}
