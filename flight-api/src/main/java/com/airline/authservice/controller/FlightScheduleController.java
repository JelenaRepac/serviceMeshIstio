package com.airline.authservice.controller;

import com.airline.authservice.model.FlightSchedule;
import com.airline.authservice.service.FlightScheduleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/flightSchedule")
public class FlightScheduleController {

    private final FlightScheduleService flightScheduleService;

    public FlightScheduleController(FlightScheduleService flightScheduleService) {
        this.flightScheduleService = flightScheduleService;
    }

    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<FlightSchedule>> getAll() {
        return new ResponseEntity<>(flightScheduleService.getAllFlightSchedules(), HttpStatus.CREATED);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FlightSchedule> createFlightSchedule(@RequestBody FlightSchedule flightScheduleInputDto) {
        FlightSchedule createdSchedule = flightScheduleService.addFlightSchedule(flightScheduleInputDto);
        return ResponseEntity.ok(createdSchedule);
    }

}
