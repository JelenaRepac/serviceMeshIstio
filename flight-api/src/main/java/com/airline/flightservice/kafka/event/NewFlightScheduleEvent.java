package com.airline.flightservice.kafka.event;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewFlightScheduleEvent {
    private Long id;
    private String from;
    private String to;
    private String departureTime;
    private String arrivalTime;
}
