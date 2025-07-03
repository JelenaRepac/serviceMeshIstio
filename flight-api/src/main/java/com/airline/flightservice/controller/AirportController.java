package com.airline.flightservice.controller;

import com.airline.flightservice.model.Airport;
import com.airline.flightservice.model.Country;
import com.airline.flightservice.service.AirportService;
import com.airline.flightservice.service.CountryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/airport")
public class AirportController {

    private final AirportService airportService;

    public AirportController(AirportService airportService) {
        this.airportService = airportService;
    }


    @GetMapping
    public List<Airport> getAirports() {
        return airportService.findAll();
    }

    @GetMapping("/country")
    public List<Airport> getAirports(@RequestParam("countryCode") String countryCode) {
        return airportService.getByCountryCode(countryCode);
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