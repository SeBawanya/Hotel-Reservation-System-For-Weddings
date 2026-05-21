package com.example.hotelreservationsysyemforweddings.service;

import com.example.hotelreservationsysyemforweddings.model.OptionalService;
import com.example.hotelreservationsysyemforweddings.model.WeddingPackage;
import com.example.hotelreservationsysyemforweddings.repository.WeddingPackageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class WeddingPackageService {

    @Autowired
    private WeddingPackageRepository weddingPackageRepository;

    public List<WeddingPackage> getAllPackages() {
        return weddingPackageRepository.findAll();
    }

    @Transactional
    public WeddingPackage createPackage(WeddingPackage weddingPackage) {
        // This sets up the important link from the optional service back to the package
        if (weddingPackage.getOptionalServices() != null) {
            for (OptionalService service : weddingPackage.getOptionalServices()) {
                service.setWeddingPackage(weddingPackage);
            }
        }
        return weddingPackageRepository.save(weddingPackage);
    }

    @Transactional
    public Optional<WeddingPackage> updatePackage(Long id, WeddingPackage packageDetails) {
        // Find the existing package in the database
        return weddingPackageRepository.findById(id).map(existingPackage -> {
            // Update the basic details
            existingPackage.setName(packageDetails.getName());
            existingPackage.setPrice(packageDetails.getPrice());
            existingPackage.setServices(packageDetails.getServices());
            existingPackage.setDescription(packageDetails.getDescription());

            // This is the key part:
            // 1. Clear the old list of optional services.
            // 2. Add the new list of services from the request.
            // This cleanly handles any additions, removals, or changes to the add-ons.
            existingPackage.getOptionalServices().clear();
            if (packageDetails.getOptionalServices() != null) {
                for (OptionalService newService : packageDetails.getOptionalServices()) {
                    newService.setWeddingPackage(existingPackage);
                    existingPackage.getOptionalServices().add(newService);
                }
            }

            // Save the updated package and all its related services in one transaction
            return weddingPackageRepository.save(existingPackage);
        });
    }

    public void deletePackage(Long id) {
        weddingPackageRepository.deleteById(id);
    }
}

