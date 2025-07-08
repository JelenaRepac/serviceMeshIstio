package com.airline.flightservice.service.impl;

import com.airline.flightservice.model.Country;
import com.airline.flightservice.repository.CountryRepository;
import com.airline.flightservice.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CountryServiceImpl implements CountryService {

    @Autowired
    private CountryRepository countryRepository;

    @Override
    public List<Country> findAll() {
        return countryRepository.findAll();
    }
}