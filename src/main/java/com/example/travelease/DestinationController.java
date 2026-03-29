package com.example.travelease;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/api/destinations")
public class DestinationController {

    @Autowired
    private DestinationRepository destinationRepository;

    @GetMapping
    public List<Destination> getAllDestinations() {
        return destinationRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Destination> getDestinationById(@PathVariable Long id) {
        return destinationRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Destination addDestination(@RequestBody Destination destination) {
        return destinationRepository.save(destination);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Destination> updateDestination(@PathVariable Long id,
            @RequestBody Destination updatedDestination) {
        return destinationRepository.findById(id).map(destination -> {
            destination.setName(updatedDestination.getName());
            destination.setCountry(updatedDestination.getCountry());
            destination.setBestTimeToVisit(updatedDestination.getBestTimeToVisit());
            destination.setActivities(updatedDestination.getActivities());
            destination.setAttractions(updatedDestination.getAttractions());
            destination.setDescription(updatedDestination.getDescription());
            destination.setImageUrl(updatedDestination.getImageUrl());
            return ResponseEntity.ok(destinationRepository.save(destination));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDestination(@PathVariable Long id) {
        return destinationRepository.findById(id).map(destination -> {
            destinationRepository.delete(destination);
            return ResponseEntity.ok().build();
        }).orElse(ResponseEntity.notFound().build());
    }
}