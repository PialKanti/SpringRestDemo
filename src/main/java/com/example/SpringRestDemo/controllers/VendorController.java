package com.example.SpringRestDemo.controllers;

import com.example.SpringRestDemo.entities.Vendor;
import com.example.SpringRestDemo.repositories.VendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/vendors")
public class VendorController {
    private final VendorRepository repository;

    @Autowired
    public VendorController(VendorRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<List<Vendor>> getAll() {
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Vendor> get(@PathVariable Long id) {
        Optional<Vendor> optionalVendor = repository.findById(id);
        return optionalVendor.map(ResponseEntity::ok).orElse(ResponseEntity.badRequest().build());
    }

    @PostMapping
    public ResponseEntity<Vendor> create(@RequestBody Vendor vendor) {
        Vendor createdEntity = repository.save(vendor);
        String uriString = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdEntity.getId())
                .toUriString();

        return ResponseEntity.created(URI.create(uriString))
                .body(createdEntity);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Vendor> update(@PathVariable Long id, @RequestBody Vendor vendor) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        if (!id.equals(vendor.getId())) {
            return ResponseEntity.badRequest().build();
        }

        Vendor updatedEntity = repository.save(vendor);

        return ResponseEntity.ok(updatedEntity);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        repository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
