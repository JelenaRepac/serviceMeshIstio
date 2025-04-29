package com.airline.authservice.controller;

import com.airline.authservice.service.impl.AviationStackServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/aviation")
@RequiredArgsConstructor
public class AviationStackController {

    private final AviationStackServiceImpl aviationStackService;
    @Scheduled(fixedRate = 86400000)
    @GetMapping("/countries")
    public Mono<String> getCountries() {
        return aviationStackService.getCountries();
    }

    @GetMapping("/cities")
    public Mono<String> getCities(@RequestParam String countryIso)  {
        return aviationStackService.getCities(countryIso);
    }

    @GetMapping("/airports")
    public Mono<String> getAirports(@RequestParam String iataCode) {
        return aviationStackService.getAirportsByCityIata(iataCode);
    }

    @GetMapping("/flights")
    public Mono<String> getFlights(
            @RequestParam String iataCode,
            @RequestParam String type,
            @RequestParam String date) {
        return aviationStackService.getFlights(iataCode, type, date);
    }
}

