package com.airline.flightservice.service.impl;

import com.airline.flightservice.dto.City;
import com.airline.flightservice.dto.Country;
import com.airline.flightservice.model.*;
import com.airline.flightservice.repository.FlightRepository;
import com.airline.flightservice.service.FlightService;

import org.hibernate.annotations.Cache;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FlightServiceImpl implements FlightService {
    @Value("${external.api.url}")
    private String externalApiUrl;
    private final String productServiceUrl = "http://product:9090/product/"; // URL of the ProductService
    private final RestTemplate restTemplate;

    private final FlightRepository flightRepository;

    public FlightServiceImpl(RestTemplate restTemplate, FlightRepository flightRepository) {
        this.restTemplate = restTemplate;
        this.flightRepository = flightRepository;
    }


    @Override
    public Flight save(Flight flight) {
        flight.setAvailableSeats(flight.getAirplane().getCapacity());
        return flightRepository.save(flight);
    }

    @Override
    public List<Country> getCountries(String accessKey) {
        // Pozivanje eksternog API-ja
        String url = externalApiUrl + "/countries?access_key=" + accessKey;

        // Uporaba generičkog ApiResponse sa specifičnim tipom Country
        ParameterizedTypeReference<ApiResponse<Country>> responseType = new ParameterizedTypeReference<>() {};
        ResponseEntity<ApiResponse<Country>> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                responseType
        );

        ApiResponse<Country> apiResponse = responseEntity.getBody();
        if (apiResponse != null && apiResponse.getData() != null) {
            System.out.println(apiResponse.getData().get(0));

            // Filtriranje samo potrebnih podataka
            return apiResponse.getData().stream()
                    .map(country -> {
                        Country response = new Country();
                        response.setCountryName(country.getCountryName());
                        response.setCountryId(country.getCountryId());
                        return response;
                    })
                    .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

    @Override
    @Cacheable(value = "flightDataCache")
    public List<City> getCities(String accessKey) {
        // Pozivanje eksternog API-ja
        String url = externalApiUrl + "/cities?access_key=" + accessKey;

        ParameterizedTypeReference<ApiResponse<City>> responseType = new ParameterizedTypeReference<>() {};
        ResponseEntity<ApiResponse<City>> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                responseType
        );

        ApiResponse<City> apiResponse = responseEntity.getBody();

        if (apiResponse != null && apiResponse.getData() != null) {
            System.out.println(apiResponse.getData().get(0));

            // Filtriranje samo potrebnih podataka
            return apiResponse.getData().stream()
                    .map(city -> {
                        City response = new City();
                        response.setId(city.getId());
                        response.setName(city.getName());
                        response.setIataCode(city.getIataCode());
                        return response;
                    })
                    .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }


    @Override
    public List<Airport> getAirports(String accessKey) {
        // Pozivanje eksternog API-ja
        String url = externalApiUrl + "/cities/?access_key=" + accessKey;
        ApiResponse apiResponse = restTemplate.getForObject(url, ApiResponse.class);

//        System.out.println(apiResponse.getData().get(0));
//        // Filtriranje samo potrebnih podataka
//        return apiResponse.getData().stream()
//                .map(country -> {
//                    Country response = new Country();
//                    response.setCountryName(country.getCountryName());
//                    response.setCountryId(country.getCountryId());
//                    return response;
//                })
//                .collect(Collectors.toList());
        return null;
    }
}