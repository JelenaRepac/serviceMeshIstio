package com.airlines.airlinesharedmodule;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "flight", name = "flight_schedule")
public class FlightSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String status;
    private LocalDate arrivalDate;
    private LocalDate departureDate;
    private LocalTime departureTime;
    private LocalTime arrivalTime;
    private String startAirport;
    private String endAirport;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "flight_details_id")
    private FlightInformation flightInformation;


}