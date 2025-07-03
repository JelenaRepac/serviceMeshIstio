package com.airline.notificationservice.kafka.event;

import com.airline.notificationservice.service.EmailSenderService;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class UpcomingFlightNotificationConsumer {

    @Autowired
    private EmailSenderService emailSenderService;

    @KafkaListener(topics = "upcoming-flight-topic", groupId = "notification-group",
            containerFactory = "upcomingFlightKafkaListenerContainerFactory"
    )
    public void handleUpcomingFlightNotification(UpcomingFlightNotificationEvent event) throws MailjetSocketTimeoutException, MailjetException {
        String subjet="Upcoming Flight Reminder";
        String message = String.format(
                "Dear passenger,\n\nYour flight %s is scheduled for %s at %s.\nThis is a reminder that your flight is in %d day(s).\n\nThank you for choosing us.",
                event.getFlightNumber(),
                event.getDepartureDate(),
                event.getDepartureTime(),
                event.getDaysLeft()
        );
        emailSenderService.sendNotificationMail(event.getEmail(), subjet, message);

    }



}
