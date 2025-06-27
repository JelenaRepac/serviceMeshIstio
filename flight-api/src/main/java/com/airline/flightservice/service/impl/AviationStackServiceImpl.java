package com.airline.flightservice.service.impl;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AviationStackServiceImpl {


    private final WebClient webClient;

    @Value("${aviationstack.api.key}")
    private String apiKey;

    @Value("${aviationstack.base.url}")
    private String baseUrl;

    public Mono<String> getCountries() {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/countries")
                        .queryParam("access_key", apiKey)
                        .build())
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<String> getCities(String countryIso ) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/cities")
                        .queryParam("access_key", apiKey)
                        .queryParam("country_iso2", countryIso)
                        .build())
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<String> getAirportsByCityIata(String iataCode) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/airports")
                        .queryParam("access_key", apiKey)
                        .queryParam("iata_code", iataCode)
                        .build())
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<String> getFlights(String iataCode, String type, String date) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/flightsFuture")
                        .queryParam("access_key", apiKey)
                        .queryParam("iataCode", iataCode)
                        .queryParam("type", type)
                        .queryParam("date", date)
                        .build())
                .retrieve()
                .bodyToMono(String.class);
    }
}

