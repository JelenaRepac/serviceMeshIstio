package com.airline.reservationservice.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FlightScheduleSeatResponse {

    private Long id;
    private String seatType;
    private Boolean bookingStatus;
    private String seatNumber;
    private Long flightScheduleId;

}
