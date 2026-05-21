// CREATE NEW FILE: src/main/java/com/example/hotelreservationsysyemforweddings/controller/VendorController.java
package com.example.hotelreservationsysyemforweddings.controller;

import com.example.hotelreservationsysyemforweddings.model.Vendor;
import com.example.hotelreservationsysyemforweddings.model.VendorCategory;
import com.example.hotelreservationsysyemforweddings.repository.VendorCategoryRepository;
import com.example.hotelreservationsysyemforweddings.repository.VendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vendors")
public class VendorController {

    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private VendorCategoryRepository vendorCategoryRepository;

    // Endpoint for booking page to get all categories with their vendors
    @GetMapping
    public List<VendorCategory> getAllCategoriesWithVendors() {
        return vendorCategoryRepository.findAll();
    }

    // Endpoint for admin page to manage categories
    @PostMapping("/categories")
    @PreAuthorize("hasAnyRole('COORDINATOR', 'HOTEL_MANAGER', 'IT_OFFICER')")
    public VendorCategory createCategory(@RequestBody VendorCategory category) {
        // Add default categories
        if(vendorCategoryRepository.findByName(category.getName()).isEmpty()) {
            return vendorCategoryRepository.save(category);
        }
        return category;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('COORDINATOR', 'HOTEL_MANAGER', 'IT_OFFICER')")
    public Vendor createVendor(@RequestBody Vendor vendor) {
        return vendorRepository.save(vendor);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('COORDINATOR', 'HOTEL_MANAGER', 'IT_OFFICER')")
    public ResponseEntity<Vendor> updateVendor(@PathVariable Long id, @RequestBody Vendor vendorDetails) {
        return vendorRepository.findById(id)
                .map(vendor -> {
                    vendor.setName(vendorDetails.getName());
                    vendor.setPrice(vendorDetails.getPrice());
                    vendor.setContactInfo(vendorDetails.getContactInfo());
                    return ResponseEntity.ok(vendorRepository.save(vendor));
                }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('COORDINATOR', 'HOTEL_MANAGER', 'IT_OFFICER')")
    public ResponseEntity<?> deleteVendor(@PathVariable Long id) {
        vendorRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}