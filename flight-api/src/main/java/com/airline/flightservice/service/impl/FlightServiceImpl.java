package com.airline.flightservice.service.impl;

import com.airline.flightservice.dto.Country;
import com.airline.flightservice.model.ApiResponse;
import com.airline.flightservice.model.Flight;
import com.airline.flightservice.model.Order;
import com.airline.flightservice.model.Product;
import com.airline.flightservice.repository.FlightRepository;
import com.airline.flightservice.service.FlightService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
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

    public List<Product> createOrder(Order order) {
        List<Long> productIds = order.getProductIds();
        List<Product> productList = new ArrayList<>();
        productIds.forEach(productId -> {
            String productUrl = productServiceUrl + productId;
            Product product = restTemplate.getForObject(productUrl, Product.class);
            productList.add(product);


            System.out.println("Fetched Product: " + product);
        });

        return productList;
    }

    public Order getOrder(Long orderId) {
        return new Order(orderId, "John Doe", List.of(1L, 2L)); // Example
    }

    @Override
    public Flight save(Flight flight) {
        flight.setAvailableSeats(flight.getAirplane().getCapacity());
        return flightRepository.save(flight);
    }


    public List<Country> getCountries(String accessKey) {
        // Pozivanje eksternog API-ja
        String url = externalApiUrl + "?access_key=" + accessKey;
        ApiResponse apiResponse = restTemplate.getForObject(url, ApiResponse.class);

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

}