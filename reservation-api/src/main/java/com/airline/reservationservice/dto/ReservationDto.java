package com.airline.reservationservice.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReservationDto {

    private Long id;
    private Long flightScheduleId;
    private String seatNumber;
    private Long userId;
    private Boolean confirmed;
    private LocalDateTime reservedAt;

}