package com.example.hotelreservationsysyemforweddings.repository;

import com.example.hotelreservationsysyemforweddings.model.WeddingPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeddingPackageRepository extends JpaRepository<WeddingPackage, Long> {
}
