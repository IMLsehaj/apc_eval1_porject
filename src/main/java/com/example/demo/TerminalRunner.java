package com.example.demo;

import java.util.Optional;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class TerminalRunner implements CommandLineRunner {

    @Autowired
    private ShipmentService shipmentService;

    @Override
    public void run(String... args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=============================================");
        System.out.println("   Logistics and Delivery Tracking System    ");
        System.out.println("=============================================");

        while (true) {
            printMenu();
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    createShipment(scanner);
                    break;
                case "2":
                    trackShipment(scanner);
                    break;
                case "3":
                    updateShipmentStatus(scanner);
                    break;
                case "4":
                    System.out.println("Exiting application. Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void printMenu() {
        System.out.println("\n--- Main Menu ---");
        System.out.println("1. Create a new Shipment");
        System.out.println("2. Track a Shipment");
        System.out.println("3. Update Shipment Status");
        System.out.println("4. Exit");
        System.out.print("Enter your choice: ");
    }

    private void createShipment(Scanner scanner) {
        System.out.print("Enter origin: ");
        String origin = scanner.nextLine();
        System.out.print("Enter destination: ");
        String destination = scanner.nextLine();

        Shipment shipment = shipmentService.createShipment(origin, destination);
        System.out.println("\n--- Shipment Created Successfully! ---");
        System.out.println("Tracking Number: " + shipment.getTrackingNumber());
        System.out.println("Status: " + shipment.getStatus());
    }

    private void trackShipment(Scanner scanner) {
        System.out.print("Enter tracking number: ");
        String trackingNumber = scanner.nextLine();
        Optional<Shipment> shipmentOpt = shipmentService.trackShipment(trackingNumber);

        if (shipmentOpt.isPresent()) {
            Shipment s = shipmentOpt.get();
            System.out.println("\n--- Shipment Details ---");
            System.out.println("Tracking Number: " + s.getTrackingNumber());
            System.out.println("Origin: " + s.getOrigin());
            System.out.println("Destination: " + s.getDestination());
            System.out.println("Status: " + s.getStatus());
            System.out.println("Last Updated: " + s.getLastUpdatedDate());
        } else {
            System.out.println("Shipment with tracking number '" + trackingNumber + "' not found.");
        }
    }
    
    private void updateShipmentStatus(Scanner scanner) {
        System.out.print("Enter tracking number: ");
        String trackingNumber = scanner.nextLine();
        
        System.out.println("Select new status:");
        for (TrackingStatus status : TrackingStatus.values()) {
            System.out.println("- " + status.name());
        }
        System.out.print("Enter new status: ");
        String statusInput = scanner.nextLine();
        
        try {
            TrackingStatus newStatus = TrackingStatus.valueOf(statusInput.toUpperCase());
            Optional<Shipment> updatedShipment = shipmentService.updateShipmentStatus(trackingNumber, newStatus);
            if(updatedShipment.isPresent()) {
                System.out.println("\n--- Shipment Status Updated! ---");
                System.out.println("New Status: " + updatedShipment.get().getStatus());
            } else {
                 System.out.println("Shipment with tracking number '" + trackingNumber + "' not found.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid status provided. Please enter a valid status from the list.");
        }
    }
}

