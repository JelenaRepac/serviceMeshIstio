package com.airline.flightservice.service;

import com.airline.flightservice.dto.City;
import com.airline.flightservice.dto.Country;
import com.airline.flightservice.model.Airport;
import com.airline.flightservice.model.Flight;

import java.util.List;

public interface FlightService {
    Flight save(Flight flight);
     List<Country> getCountries(String accessKey);
     List<Airport> getAirports(String accessKey);
    List<City> getCities(String accessKey);


}
