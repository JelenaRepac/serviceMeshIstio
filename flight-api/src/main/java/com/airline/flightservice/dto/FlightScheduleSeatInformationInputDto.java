package com.airline.flightservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class FlightScheduleSeatInformationInputDto {
    private Long id;
    private String seatType;
    private Boolean bookingStatus;
    private Long flightScheduleId;
    private String seatNumber;
}