package com.airline.flightservice.service.impl;

import com.airline.flightservice.model.Airport;
import com.airline.flightservice.model.Country;
import com.airline.flightservice.repository.AirportRepository;
import com.airline.flightservice.repository.CountryRepository;
import com.airline.flightservice.service.AirportService;
import com.airline.flightservice.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AirportServiceImpl implements AirportService {

    @Autowired
    private AirportRepository airportRepository;

    @Override
    public List<Airport> findAll() {
        return airportRepository.findAll();
    }

    @Override
    public List<Airport> getByCountryCode(String countryCode) {
        return airportRepository.findByCountry(countryCode);
    }
}