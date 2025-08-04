

package com.airlines.airlinesharedmodule;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "flight", name = "flight_price")
public class FlightPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double price;

    private String currency;

    private LocalDate validFrom;

    private LocalDate validTo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "flight_schedule_id")
    @JsonBackReference
    private FlightSchedule flightSchedule;

}
