package com.airline.flightservice.model;

import com.airlines.airlinesharedmodule.FlightSchedule;
import lombok.*;

import jakarta.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "flight", name = "flight_schedule_seat")
public class FlightScheduleSeatInformation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
//    private String seatId;
    private String seatType;
    private Boolean bookingStatus;

    private String seatNumber;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "flight_schedule_id")
    private FlightSchedule flightSchedule;
}
