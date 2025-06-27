package com.airline.flightservice.dto;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FlightScheduleFilter {
        private String from;
        private String to;

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        private LocalDate departureDate;

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        private LocalDate arrivalDate;
    }


