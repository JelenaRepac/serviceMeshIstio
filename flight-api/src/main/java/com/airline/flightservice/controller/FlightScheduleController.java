package com.airline.flightservice.controller;

import com.airline.flightservice.dto.FlightScheduleFilter;
import com.airline.flightservice.model.FlightSchedule;
import com.airline.flightservice.service.FlightScheduleService;
import com.airline.flightservice.specification.FlightScheduleSpecification;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/flightSchedule")
public class FlightScheduleController {

    private final FlightScheduleService flightScheduleService;

    public FlightScheduleController(FlightScheduleService flightScheduleService) {
        this.flightScheduleService = flightScheduleService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<FlightSchedule>> getAll(
            @RequestParam(value = "from", required = false) String from,
            @RequestParam(value = "to", required = false) String to,
            @RequestParam(value = "departureDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate departureDate,
            @RequestParam(value = "arrivalDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate arrivalDate
    ) {
        FlightScheduleFilter filter = new FlightScheduleFilter();
        filter.setFrom(from);
        filter.setTo(to);
        filter.setDepartureDate(departureDate);
        filter.setArrivalDate(arrivalDate);

        boolean hasFilter = from != null || to != null || departureDate != null || arrivalDate != null;

        if (hasFilter) {
            List<FlightSchedule> results = flightScheduleService.searchSchedules(filter);
            return ResponseEntity.ok(results);
        }

        return new ResponseEntity<>(flightScheduleService.getAllFlightSchedules(), HttpStatus.OK);
    }



    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FlightSchedule> createFlightSchedule(@RequestBody FlightSchedule flightScheduleInputDto) {
        FlightSchedule createdSchedule = flightScheduleService.addFlightSchedule(flightScheduleInputDto);
        return ResponseEntity.ok(createdSchedule);
    }

}
