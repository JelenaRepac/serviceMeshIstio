package com.airline.reservationservice.service;

import com.airline.reservationservice.common.MailType;

import java.time.LocalDate;
import java.time.LocalTime;

public interface RemoteEmailSenderService {

    void sendConfirmationEmail(String email, String token, MailType mailType);

    void sendNotificationMail(String email, LocalDate departureDate, LocalTime departureTime, Long daysBefore)
            ;

}
