package com.example.SpringRestDemo.controllers;

import com.example.SpringRestDemo.entities.Product;
import com.example.SpringRestDemo.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductRepository repository;

    @GetMapping
    public ResponseEntity<List<Product>> getAll() {
        return ResponseEntity.ok(repository.findAll());
    }

    @PostMapping
    public ResponseEntity<Product> create(@RequestBody Product product) throws URISyntaxException {
        repository.save(product);
        return ResponseEntity.created(new URI("/products/" + product.getId())).body(product);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Product> get(@PathVariable Long id) {
        Optional<Product> product = repository.findById(id);
        return product.map(ResponseEntity::ok).orElse(ResponseEntity.badRequest().build());
    }
}
