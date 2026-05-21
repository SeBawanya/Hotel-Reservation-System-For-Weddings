package com.example.hotelreservationsysyemforweddings.controller;

import com.example.hotelreservationsysyemforweddings.model.WeddingPackage;
import com.example.hotelreservationsysyemforweddings.service.WeddingPackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/packages")
public class WeddingPackageController {

    @Autowired
    private WeddingPackageService weddingPackageService;

    @GetMapping
    public List<WeddingPackage> getAllPackages() {
        return weddingPackageService.getAllPackages();
    }

    @PostMapping
    public WeddingPackage createPackage(@RequestBody WeddingPackage weddingPackage) {
        // Calls the new createPackage method in the service
        return weddingPackageService.createPackage(weddingPackage);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WeddingPackage> updatePackage(@PathVariable Long id, @RequestBody WeddingPackage packageDetails) {
        // Uses the more robust updatePackage method which returns an Optional
        return weddingPackageService.updatePackage(id, packageDetails)
                .map(ResponseEntity::ok) // If package is found and updated, return 200 OK with the package
                .orElse(ResponseEntity.notFound().build()); // If package is not found, return 404 Not Found
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePackage(@PathVariable Long id) {
        weddingPackageService.deletePackage(id);
        return ResponseEntity.ok().build();
    }
}

