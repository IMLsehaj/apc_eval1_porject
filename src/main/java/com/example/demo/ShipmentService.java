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

    public Shipment createShipment(String origin, String destination) {
        Shipment shipment = new Shipment();
        shipment.setOrigin(origin);
        shipment.setDestination(destination);
        shipment.setTrackingNumber(UUID.randomUUID().toString());
        shipment.setStatus(TrackingStatus.PENDING);
        shipment.setCreatedDate(LocalDateTime.now());
        shipment.setLastUpdatedDate(LocalDateTime.now());
        return shipmentRepository.save(shipment);
    }

    public Optional<Shipment> trackShipment(String trackingNumber) {
        return shipmentRepository.findByTrackingNumber(trackingNumber);
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
}
