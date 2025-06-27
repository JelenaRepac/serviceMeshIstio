package com.airline.flightservice.service;

import com.airline.flightservice.dto.City;
import com.airline.flightservice.dto.FlightInformationDto;

import java.util.List;

public interface FlightService {

    List<City> getCities(String accessKey);

     FlightInformationDto addFlight(FlightInformationDto flightInformationDto);

    FlightInformationDto getFlightById(Long id);

    List<FlightInformationDto> getAllFlights();

    FlightInformationDto updateFlight(Long id, FlightInformationDto flightInformationDto);

    void deleteFlight(Long id);


}
