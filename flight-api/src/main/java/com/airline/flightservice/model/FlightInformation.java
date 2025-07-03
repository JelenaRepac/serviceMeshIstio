package com.airline.flightservice.model;


import lombok.*;

import jakarta.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "flight", name = "flight_details")
public class FlightInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String flightName;
    private int capacity;
    private String flightType;
    private String seatType;
    private double maximumWeightForPassenger;
    private String airlineService;



}