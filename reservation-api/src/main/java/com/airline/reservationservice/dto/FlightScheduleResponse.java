package com.airline.reservationservice.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FlightScheduleResponse {
    private Long id;
    private LocalDate departureDate;
    private LocalTime departureTime;
}
