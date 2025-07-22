package com.airline.notificationservice.kafka.event;

import com.airline.notificationservice.common.MailType;
import com.airline.notificationservice.service.EmailSenderService;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


@Component
public class EmailEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(EmailEventConsumer.class);
    @Autowired
    private EmailSenderService emailSenderService;

    @KafkaListener(topics = "email-send-topic", groupId = "email-group", containerFactory = "kafkaListenerContainerFactory")
    public void listenEmailEvent(EmailEvent event) throws MailjetSocketTimeoutException, MailjetException {
        log.info("Received EmailEvent: {}", event);
        sendEmail(event.getEmail(), event.getToken(), event.getMailType());
    }


    private void sendEmail(String email, String token, MailType mailType) throws MailjetSocketTimeoutException, MailjetException {
        // Implementacija slanja mejla
        emailSenderService.sendEmail(email, token, mailType);
    }
}
