package com.airline.flightservice.controller;

import com.airline.flightservice.dto.City;
import com.airline.flightservice.dto.Country;
import com.airline.flightservice.model.Flight;
import com.airline.flightservice.service.impl.FlightServiceImpl;
import com.airline.flightservice.model.Product;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/flight")
//@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class FlightController {

    private final FlightServiceImpl flightService;

    public FlightController(FlightServiceImpl orderService) {
        this.flightService = orderService;
    }

//    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value="/add",consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveFlight(@RequestBody Flight flight){ return new ResponseEntity<>(flightService.save(flight), HttpStatus.CREATED); }


    @GetMapping("/countries")
    public List<Country> getCountries(@RequestParam String accessKey) {
        return flightService.getCountries(accessKey);
    }


    @GetMapping("/airports")
    public List<Country> getAirports(@RequestParam String accessKey) {
        return flightService.getCountries(accessKey);
    }
    @GetMapping("/cities")
    public List<City> getCities(@RequestParam String accessKey) {
        return flightService.getCities(accessKey);
    }

}