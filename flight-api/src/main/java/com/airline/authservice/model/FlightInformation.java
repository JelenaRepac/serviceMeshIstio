package com.airline.authservice.model;


import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "flights", name = "flight_details")
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