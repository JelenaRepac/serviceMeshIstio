package com.airline.flightservice.controller;

import com.airline.flightservice.dto.FlightInformationDto;
import com.airline.flightservice.model.Country;
import com.airline.flightservice.service.CountryService;
import com.airline.flightservice.service.impl.CountryServiceImpl;
import com.airline.flightservice.service.impl.FlightServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/country")
public class CountryController {

    private final CountryService countryService;

    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }


    @GetMapping
    public List<Country> getCountries() {
        return countryService.findAll();
    }


//    @GetMapping("/airport")
//    public List<Airport> getAirports(@RequestParam String accessKey) {
//        return flightService.getAirports(accessKey);
//    }
//    @GetMapping("/cities")
//    public List<City> getCities(@RequestParam String accessKey, @RequestParam String iso) {
//        return flightService.findCitiesByIso(accessKey, iso);
//    }

}