package com.airline.flightservice.service;

import com.airline.flightservice.dto.City;
import com.airline.flightservice.dto.FlightInformationDto;

import java.util.List;

public interface FlightService {


     FlightInformationDto addFlight(FlightInformationDto flightInformationDto);

    FlightInformationDto getFlightById(Long id);

    List<FlightInformationDto> getAllFlights();


    void deleteFlight(Long id);


}
