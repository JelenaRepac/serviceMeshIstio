package com.airline.flightservice.service.impl;

import com.airline.flightservice.model.Flight;
import com.airline.flightservice.model.Order;
import com.airline.flightservice.model.Product;
import com.airline.flightservice.repository.FlightRepository;
import com.airline.flightservice.service.FlightService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class FlightServiceImpl implements FlightService {

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
}