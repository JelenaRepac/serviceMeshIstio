package com.airline.pricingservice.service.impl;

import com.airline.pricingservice.common.MailType;
import com.airline.pricingservice.common.PaymentStatus;
import com.airline.pricingservice.kafka.EmailEvent;
import com.airline.pricingservice.model.Payment;
import com.airline.pricingservice.repository.PaymentRepository;
import com.airline.pricingservice.service.RemoteEmailSenderService;
import com.airlines.airlinesharedmodule.Voucher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;

//Servis za pozivanje notification-api-a
@Service
@RequiredArgsConstructor
@Slf4j
public class RemoteEmailSenderServiceImpl implements RemoteEmailSenderService {
    @Autowired
    private PaymentRepository paymentRepository;


    @Autowired
    private KafkaTemplate<String, EmailEvent> kafkaTemplate;


    private final String emailTopic = "email-send-topic";

    @Autowired
    private  WebClient webClient;

    @Override
    public void sendEmail(String email, String token, MailType mailType) {
        log.info("SEND MAIL");
        log.info("REQUEST: { email:{}, token:{}, type:{}}", email, token, mailType);

        //UPDATE PAYMENT

        Payment payment = paymentRepository.findTopByUserEmailOrderByCreatedAtDesc(email)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found"));

        payment.setStatus(PaymentStatus.SUCCESS.name());
        payment.setUpdatedAt(LocalDateTime.now());
        paymentRepository.save(payment);




        EmailEvent event = new EmailEvent(email, token, mailType);

        kafkaTemplate.send(emailTopic, event);
        log.info("Kafka message for email sending queued");

    }




}
