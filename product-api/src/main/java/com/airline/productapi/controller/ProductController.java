package com.airline.productapi.controller;

import com.airline.productapi.model.Product;
import com.airline.productapi.service.ProductServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    private final  ProductServiceImpl productService;

    public ProductController(ProductServiceImpl productService) {
        this.productService = productService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Product>  createProduct(@RequestBody Product product) {
        System.out.println(product.toString());
        return new ResponseEntity<>(productService.saveProduct(product), HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) throws Exception {
        return productService.getProductById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        try {
            return ResponseEntity.ok(productService.updateProduct(id, product));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
