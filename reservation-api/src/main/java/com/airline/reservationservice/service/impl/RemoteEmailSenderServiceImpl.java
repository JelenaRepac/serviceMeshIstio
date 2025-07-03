package com.airline.reservationservice.service.impl;

import com.airline.reservationservice.common.MailType;
import com.airline.reservationservice.kafka.EmailEvent;
import com.airline.reservationservice.kafka.UpcomingFlightNotificationEvent;
import com.airline.reservationservice.service.RemoteEmailSenderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;

//Servis za pozivanje notification-api-a
@Service
@RequiredArgsConstructor
@Slf4j
public class RemoteEmailSenderServiceImpl implements RemoteEmailSenderService {


    @Autowired
    private KafkaTemplate<String, EmailEvent> kafkaTemplate;

    @Autowired
    private KafkaTemplate<String, UpcomingFlightNotificationEvent> kafkaTemplateUpcomingFlightNotification;


    private final String emailTopic = "email-send-topic";

    @Value("${email-service.url}")
    private String emailServiceUrl;

    @Override
    public void sendConfirmationEmail(String email, String token, MailType mailType) {
        log.info("SEND CONFIRMATION MAIL");
        log.info("REQUEST: { email:{}, token:{}, type:{}}", email, token, mailType);

        EmailEvent event = new EmailEvent(email, token, mailType);

        kafkaTemplate.send(emailTopic, event);
        log.info("Kafka message for email sending queued");

    }

    @Override
    public void sendNotificationMail(String email, LocalDate departureDate, LocalTime departureTime, Long daysBefore) {
        log.info("SEND NOTIFICATION MAIL");

        UpcomingFlightNotificationEvent event = new UpcomingFlightNotificationEvent(
                email,
                departureDate,
                departureTime ,
                daysBefore
        );
        kafkaTemplateUpcomingFlightNotification.send("upcoming-flight-topic", event);
        log.info("Kafka message for email sending queued");

    }
}
