package com.airlines.airlinesharedmodule;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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