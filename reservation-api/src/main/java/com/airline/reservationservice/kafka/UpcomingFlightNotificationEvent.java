package com.airline.reservationservice.kafka;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpcomingFlightNotificationEvent {

    private String email;
    private LocalDate departureDate;
    private LocalTime departureTime;
    private long daysLeft; // 7, 3 ili 1

}
