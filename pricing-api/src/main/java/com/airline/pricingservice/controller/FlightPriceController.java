package com.airline.pricingservice.controller;


import com.airline.pricingservice.service.impl.FlightPriceService;
import com.airlines.airlinesharedmodule.FlightPrice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/price")
public class FlightPriceController {

    private final FlightPriceService flightPriceService;

    public FlightPriceController(FlightPriceService flightPriceService) {
        this.flightPriceService = flightPriceService;
    }

    @GetMapping("/schedule/{scheduleId}")
    public ResponseEntity<List<FlightPrice>> getPricesBySchedule(@PathVariable Long scheduleId) {
        List<FlightPrice> prices = flightPriceService.getPricesByFlightScheduleId(scheduleId);
        return ResponseEntity.ok(prices);
    }

    @PostMapping
    public ResponseEntity<FlightPrice> createPrice(@RequestBody FlightPrice price) {
        FlightPrice saved = flightPriceService.savePrice(price);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePrice(@PathVariable Long id) {
        flightPriceService.deletePrice(id);
        return ResponseEntity.noContent().build();
    }

}
