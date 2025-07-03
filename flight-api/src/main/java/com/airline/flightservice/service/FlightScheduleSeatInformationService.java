package com.airline.flightservice.service;


import com.airline.flightservice.dto.FlightScheduleSeatInformationInputDto;
import com.airline.flightservice.dto.FlightScheduleSeatInformationOutputDto;

import java.util.List;

public interface FlightScheduleSeatInformationService {
    List<FlightScheduleSeatInformationOutputDto> addSeatInformation(FlightScheduleSeatInformationInputDto inputDto);
    FlightScheduleSeatInformationOutputDto getSeatInformationById(Long id);
    List<FlightScheduleSeatInformationOutputDto> getAllSeatInformation();

    List<FlightScheduleSeatInformationOutputDto> getAllSeatInformationByFlight(String flightId);

    FlightScheduleSeatInformationOutputDto updateSeatInformation(Long id, FlightScheduleSeatInformationInputDto inputDto);
    void deleteSeatInformation(Long id);
    List<FlightScheduleSeatInformationOutputDto> getSeatInformationByFlightScheduleIdAndBookingStatusAndSeatNumber(Long flightScheduleId, Boolean bookingStatus, String seatNumber);
    List<FlightScheduleSeatInformationOutputDto> getSeatInformationBySeatType(String seatType);
}