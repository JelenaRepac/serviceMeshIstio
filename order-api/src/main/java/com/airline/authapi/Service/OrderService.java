package com.airline.authapi.Service;

import com.airline.authapi.Model.Order;
import com.airline.authapi.Model.Product;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final String productServiceUrl = "http://product:9090/product/"; // URL of the ProductService
    private final RestTemplate restTemplate;

    public OrderService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
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
}