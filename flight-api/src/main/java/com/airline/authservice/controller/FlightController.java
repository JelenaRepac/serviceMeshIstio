package com.airline.authservice.controller;

import com.airline.authservice.dto.FlightInformationDto;
import com.airline.authservice.service.impl.FlightServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
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
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FlightInformationDto> saveFlight(@RequestBody FlightInformationDto flightInformationDto) {
        return new ResponseEntity<>(flightService.addFlight(flightInformationDto), HttpStatus.CREATED);
    }


    @GetMapping(value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FlightInformationDto> getById(@PathVariable Long id) {
        return new ResponseEntity<>(flightService.getFlightById(id), HttpStatus.CREATED);
    }

    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<FlightInformationDto>> getAll() {
        return new ResponseEntity<>(flightService.getAllFlights(), HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{id}")
    public void  deleteById(@PathVariable Long id) {
        flightService.deleteFlight(id);

    }


//    @GetMapping("/countries")
//    public List<Country> getCountries(@RequestParam String accessKey,
//                                      @RequestParam Integer offset,
//                                      @RequestParam Integer limit) {
//        return flightService.getCountries(accessKey, offset, limit);
//    }


//    @GetMapping("/airports")
//    public List<Airport> getAirports(@RequestParam String accessKey) {
//        return flightService.getAirports(accessKey);
//    }
//    @GetMapping("/cities")
//    public List<City> getCities(@RequestParam String accessKey, @RequestParam String iso) {
//        return flightService.findCitiesByIso(accessKey, iso);
//    }

}