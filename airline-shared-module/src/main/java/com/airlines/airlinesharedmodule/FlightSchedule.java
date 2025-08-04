package com.airlines.airlinesharedmodule;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

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


    @OneToMany(mappedBy = "flightSchedule", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<FlightPrice> flightPrices;

}