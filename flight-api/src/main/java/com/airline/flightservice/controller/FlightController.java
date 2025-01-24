package com.airline.flightservice.controller;

import com.airline.flightservice.dto.City;
import com.airline.flightservice.dto.Country;
import com.airline.flightservice.model.tr.Airport;
import com.airline.flightservice.model.FlightInformation;
import com.airline.flightservice.service.impl.FlightServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> saveFlight(@RequestBody FlightInformation flight){ return new ResponseEntity<>(flightService.save(flight), HttpStatus.CREATED); }


    @GetMapping("/countries")
    public List<Country> getCountries(@RequestParam String accessKey,
                                      @RequestParam Integer offset,
                                      @RequestParam Integer limit) {
        return flightService.getCountries(accessKey, offset, limit);
    }


    @GetMapping("/airports")
    public List<Airport> getAirports(@RequestParam String accessKey) {
        return flightService.getAirports(accessKey);
    }
    @GetMapping("/cities")
    public List<City> getCities(@RequestParam String accessKey, @RequestParam String iso) {
        return flightService.findCitiesByIso(accessKey, iso);
    }

}