// CREATE NEW FILE: src/main/java/com/example/hotelreservationsysyemforweddings/repository/VendorRepository.java
package com.example.hotelreservationsysyemforweddings.repository;

import com.example.hotelreservationsysyemforweddings.model.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VendorRepository extends JpaRepository<Vendor, Long> {}