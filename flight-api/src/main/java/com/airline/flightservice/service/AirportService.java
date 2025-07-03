package com.airline.flightservice.service;

import com.airline.flightservice.model.Airport;
import com.airline.flightservice.model.Country;

import java.util.List;

public interface AirportService {

    List<Airport> findAll();

    List<Airport> getByCountryCode(String countryCode);
}
