package com.airlines.airlinesharedmodule;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(schema = "reservation", name = "reservation")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long flightScheduleId;
    private String seatNumber;
    private Long userId;
    private Boolean confirmed;
    private LocalDateTime reservedAt;

    private String voucherId;

}