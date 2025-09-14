package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class ShipmentService {

    @Autowired
    private ShipmentRepository shipmentRepository;
<<<<<<< HEAD
    


    public Shipment createShipment(String origin, String destination, User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        if (origin == null || origin.trim().isEmpty()) {
            throw new IllegalArgumentException("Origin cannot be null or empty");
        }
        if (destination == null || destination.trim().isEmpty()) {
            throw new IllegalArgumentException("Destination cannot be null or empty");
        }
        
        Shipment shipment = new Shipment();
        shipment.setOrigin(origin.trim());
        shipment.setDestination(destination.trim());
        shipment.setUser(user);
=======

    public Shipment createShipment(String origin, String destination) {
        Shipment shipment = new Shipment();
        shipment.setOrigin(origin);
        shipment.setDestination(destination);
>>>>>>> c6b104e9e877563a2e1a1d208f8998f68b682946
        shipment.setTrackingNumber(UUID.randomUUID().toString());
        shipment.setStatus(TrackingStatus.PENDING);
        shipment.setCreatedDate(LocalDateTime.now());
        shipment.setLastUpdatedDate(LocalDateTime.now());
<<<<<<< HEAD
        
        try {
            return shipmentRepository.save(shipment);
        } catch (Exception e) {
            throw new RuntimeException("Failed to save shipment: " + e.getMessage(), e);
        }
    }

     public Optional<Shipment> trackShipment(String trackingNumber) {
        return shipmentRepository.findByTrackingNumber(trackingNumber);
     }
    public Optional<Shipment> trackShipmentWithUser(String trackingNumber) {
    return shipmentRepository.findByTrackingNumberWithUser(trackingNumber);
=======
        return shipmentRepository.save(shipment);
    }

    public Optional<Shipment> trackShipment(String trackingNumber) {
        return shipmentRepository.findByTrackingNumber(trackingNumber);
>>>>>>> c6b104e9e877563a2e1a1d208f8998f68b682946
    }

    public Optional<Shipment> updateShipmentStatus(String trackingNumber, TrackingStatus status) {
        Optional<Shipment> shipmentOpt = shipmentRepository.findByTrackingNumber(trackingNumber);
        if (shipmentOpt.isPresent()) {
            Shipment shipment = shipmentOpt.get();
            shipment.setStatus(status);
            shipment.setLastUpdatedDate(LocalDateTime.now());
            shipmentRepository.save(shipment);
            return Optional.of(shipment);
        }
        return Optional.empty();
    }
<<<<<<< HEAD

    public Optional<Shipment> updateFeesStatus(String trackingNumber, boolean paid) {
        Optional<Shipment> shipmentOpt = shipmentRepository.findByTrackingNumber(trackingNumber);
        if (shipmentOpt.isPresent()) {
            Shipment shipment = shipmentOpt.get();
            shipment.setFeesPaid(paid);
            shipment.setLastUpdatedDate(LocalDateTime.now());
            shipmentRepository.save(shipment);
            return Optional.of(shipment);
        }
        return Optional.empty();
    }
=======
>>>>>>> c6b104e9e877563a2e1a1d208f8998f68b682946
}
