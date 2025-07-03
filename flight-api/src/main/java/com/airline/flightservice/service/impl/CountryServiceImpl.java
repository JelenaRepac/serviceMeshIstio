package com.airline.flightservice.service.impl;

import com.airline.flightservice.dto.City;
import com.airline.flightservice.dto.FlightInformationDto;
import com.airline.flightservice.exception.NotFoundException;
import com.airline.flightservice.model.Country;
import com.airline.flightservice.model.FlightInformation;
import com.airline.flightservice.repository.CountryRepository;
import com.airline.flightservice.repository.FlightRepository;
import com.airline.flightservice.service.CountryService;
import com.airline.flightservice.service.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.airline.flightservice.mapper.FlightInformationMapper.*;

@Service
public class CountryServiceImpl implements CountryService {

    @Autowired
    private CountryRepository countryRepository;

    @Override
    public List<Country> findAll() {
        return countryRepository.findAll();
    }
}