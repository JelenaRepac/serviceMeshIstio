package com.airline.productapi.config;

import com.airline.productapi.model.Product;
import com.airline.productapi.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataLoader {
//
//    @Bean(name = "productDataLoader")
//    public CommandLineRunner dataLoader(ProductRepository productRepository) {
//        return args -> {
//            if (productRepository.count() == 0) {
//                for (int i = 1; i <= 10; i++) {
//                    Product product = new Product();
//                    product.setName("Product " + i);
//                    product.setPrice(10.0 + i);
//                    product.setDescription("Description for product " + i);
//                    productRepository.save(product);
//                }
//                System.out.println("10 products inserted.");
//            } else {
//                System.out.println("Products already exist.");
//            }
//        };
//    }
}