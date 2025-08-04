package com.airline.notificationservice.kafka.event;

import com.airline.notificationservice.dto.WebSocketNotificationDto;
import com.airline.notificationservice.model.Subscriber;
import com.airline.notificationservice.service.EmailSenderService;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component
public class NewFlightScheduleConsumer {

    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Autowired
    private final SimpMessagingTemplate messagingTemplate;

    public NewFlightScheduleConsumer(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @KafkaListener(topics = "new-flight-schedule-topic", groupId = "notification-group",
            containerFactory = "newFlightKafkaListenerContainerFactory"
    )
    public void handleUpcomingFlightNotification(NewFlightScheduleEvent event) throws MailjetSocketTimeoutException, MailjetException {
//        List<Subscriber> subscribers = webClientBuilder.build()
//                .get()
//                .uri(uriBuilder -> uriBuilder
//                        .scheme("http")
//                        .host("localhost")
//                        .port(8081)
//                        .path("/api/subscription")
//                        .queryParam("status", "ACTIVE")
//                        .build())
//                .retrieve()
//                .bodyToFlux(Subscriber.class)
//                .collectList()
//                .block();
//
//
//        for (Subscriber s : subscribers) {
//            String body = """
//                    Dear %s,
//
//                    A new flight has been added:
//
//                    Flight: %s
//                    From: %s → To: %s
//                    Departure: %s
//                    Arrival: %s
//
//                    Book your seat now!
//                    """.formatted(
//                    s.getName(),
//                    event.getId(),
//                    event.getFrom(),
//                    event.getTo(),
//                    event.getDepartureTime(),
//                    event.getArrivalTime()
//            );
//
//            emailSenderService.sendNotificationMail(s.getEmail(), "New Flight Added!", body);
//        }

        //WEB SOCKET - SENDING TO FRONT APP

        messagingTemplate.convertAndSend("/topic/notifications", new WebSocketNotificationDto(
                "✈ New Flight Alert!\nFrom: %s\nTo: %s\nDeparture Time: %s".formatted(
                        event.getFrom(), event.getTo(), event.getDepartureTime()
                )
        ));

    }


}
