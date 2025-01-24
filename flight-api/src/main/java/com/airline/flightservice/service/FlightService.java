package com.airline.flightservice.service;

import com.airline.flightservice.dto.City;
import com.airline.flightservice.dto.Country;
import com.airline.flightservice.dto.FlightInformationDto;
import com.airline.flightservice.model.tr.Airport;
import com.airline.flightservice.model.FlightInformation;

import java.util.List;

public interface FlightService {
    FlightInformation save(FlightInformation flight);
     List<Country> getCountries(String accessKey,int offset, int limit) ;
     List<Airport> getAirports(String accessKey);
    List<City> getCities(String accessKey);


    List<City> findCitiesByIso(String accessKey, String iso2);



     FlightInformationDto addFlight(FlightInformationDto flightInformationDto);

    FlightInformationDto getFlightById(Long id);

    List<FlightInformationDto> getAllFlights();

    FlightInformationDto updateFlight(Long id, FlightInformationDto flightInformationDto);

    void deleteFlight(Long id);
}
