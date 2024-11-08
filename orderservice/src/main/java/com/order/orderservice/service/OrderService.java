package com.order.orderservice.service;

import com.order.orderservice.model.Order;
import com.order.orderservice.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class OrderService {

    private final String productServiceUrl = "http://localhost:9090/product/"; // URL of the ProductService
    private final RestTemplate restTemplate;

    public OrderService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Order createOrder(Order order) {
        List<Long> productIds = order.getProductIds();
        productIds.forEach(productId -> {
            String productUrl = productServiceUrl + productId;
            Product product = restTemplate.getForObject(productUrl, Product.class);
            System.out.println("Fetched Product: " + product);
        });

        return order;
    }

    public Order getOrder(Long orderId) {
        return new Order(orderId, "John Doe", List.of(1L, 2L)); // Example
    }
}