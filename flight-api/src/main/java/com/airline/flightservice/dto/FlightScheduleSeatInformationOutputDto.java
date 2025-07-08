package com.airline.flightservice.dto;


import com.airlines.airlinesharedmodule.FlightSchedule;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlightScheduleSeatInformationOutputDto {
    private Long id;
    private String seatType;
    private Boolean bookingStatus;
    private String seatNumber;
    private FlightSchedule flightSchedule;
}