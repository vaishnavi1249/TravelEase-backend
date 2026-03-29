package com.example.travelease;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/api/packages")
public class TravelPackageController {

    @Autowired
    private TravelPackageRepository travelPackageRepository;

    @GetMapping
    public List<TravelPackage> getAllPackages() {
        return travelPackageRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TravelPackage> getPackageById(@PathVariable Long id) {
        return travelPackageRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public TravelPackage addPackage(@RequestBody TravelPackage travelPackage) {
        return travelPackageRepository.save(travelPackage);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TravelPackage> updatePackage(@PathVariable Long id,
            @RequestBody TravelPackage updatedPackage) {
        return travelPackageRepository.findById(id).map(travelPackage -> {
            travelPackage.setName(updatedPackage.getName());
            travelPackage.setDestination(updatedPackage.getDestination());
            travelPackage.setPrice(updatedPackage.getPrice());
            travelPackage.setDuration(updatedPackage.getDuration());
            travelPackage.setIncludedServices(updatedPackage.getIncludedServices());
            travelPackage.setImageUrl(updatedPackage.getImageUrl());
            travelPackage.setDescription(updatedPackage.getDescription());
            return ResponseEntity.ok(travelPackageRepository.save(travelPackage));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePackage(@PathVariable Long id) {
        return travelPackageRepository.findById(id).map(travelPackage -> {
            travelPackageRepository.delete(travelPackage);
            return ResponseEntity.ok().build();
        }).orElse(ResponseEntity.notFound().build());
    }
}