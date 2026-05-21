package com.example.hotelreservationsysyemforweddings.repository;

import com.example.hotelreservationsysyemforweddings.model.VendorCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional; // Import Optional

public interface VendorCategoryRepository extends JpaRepository<VendorCategory, Long> {

    // START OF CORRECTION: Add this method definition
    Optional<VendorCategory> findByName(String name);
    // END OF CORRECTION

}