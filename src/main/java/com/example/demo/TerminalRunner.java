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

    @Autowired
    private UserRepository userRepository;

    private String loggedInUser = null;

    @Override
    public void run(String... args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=============================================");
        System.out.println("   Logistics and Delivery Tracking System    ");
        System.out.println("=============================================");

        // 🔹 User authentication loop
        while (loggedInUser == null) {
            printAuthMenu();
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    signUp(scanner);
                    break;
                case "2":
                    login(scanner);
                    break;
                case "3":
                    System.out.println("\n Exiting application. Goodbye!");
                    return;
                default:
                    System.out.println("\n Invalid choice. Please try again.");
            }
        }

        // 🔹 Main shipment operations loop
        boolean running = true;
        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();

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
                    System.out.println("\n Logging out...");
                    loggedInUser = null; 
                    run(); // restart login/signup flow
                    return;
                case "5":
                    System.out.println("\n Exiting application. Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("\n Invalid choice. Please try again.");
            }
        }
        scanner.close();
    }

    // ========================= AUTH SECTION =========================
    private void printAuthMenu() {
        System.out.println("\n--- Authentication Menu ---");
        System.out.println("1  Sign Up");
        System.out.println("2  Log In");
        System.out.println("3  Exit");
        System.out.print(" Enter your choice: ");
    }

    private void signUp(Scanner scanner) {
        System.out.print("\nEnter a new username: ");
        String username = scanner.nextLine().trim();

        if (userRepository.findByUsername(username).isPresent()) {
            System.out.println(" Username already exists. Try logging in.");
            return;
        }

        System.out.print("Enter a password: ");
        String password = scanner.nextLine().trim();

        User newUser = new User(username, password);
        userRepository.save(newUser); // ✅ Stored in SQL
        System.out.println("User registered successfully! Please login.");
    }

    private void login(Scanner scanner) {
        System.out.print("\nEnter username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Enter password: ");
        String password = scanner.nextLine().trim();

        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent() && userOpt.get().getPassword().equals(password)) {
            loggedInUser = username;
            System.out.println(" Login successful! Welcome, " + username + " ");
        } else {
            System.out.println(" Invalid username or password.");
        }
    }

    // ========================= SHIPMENT SECTION =========================
    private void printMenu() {
        System.out.println("\n--- Main Menu --- (Logged in as: " + loggedInUser + ")");
        System.out.println("1  Create a new Shipment");
        System.out.println("2  Track a Shipment");
        System.out.println("3  Update Shipment Status");
        System.out.println("4  Log Out");
        System.out.println("5  Exit");
        System.out.print(" Enter your choice: ");
    }

    private void createShipment(Scanner scanner) {
        System.out.print("\nEnter Origin: ");
        String origin = scanner.nextLine().trim();
        System.out.print("Enter Destination: ");
        String destination = scanner.nextLine().trim();

        Shipment shipment = shipmentService.createShipment(origin, destination);
        System.out.println("\n Shipment Created Successfully!");
        System.out.println("---------------------------------");
        System.out.println("Tracking Number : " + shipment.getTrackingNumber());
        System.out.println("Origin          : " + shipment.getOrigin());
        System.out.println("Destination     : " + shipment.getDestination());
        System.out.println("Status          : " + shipment.getStatus());
    }

    private void trackShipment(Scanner scanner) {
        System.out.print("\nEnter Tracking Number: ");
        String trackingNumber = scanner.nextLine().trim();

        Optional<Shipment> shipmentOpt = shipmentService.trackShipment(trackingNumber);
        if (shipmentOpt.isPresent()) {
            Shipment s = shipmentOpt.get();
            System.out.println("\n Shipment Details");
            System.out.println("---------------------------------");
            System.out.println("Tracking Number : " + s.getTrackingNumber());
            System.out.println("Origin          : " + s.getOrigin());
            System.out.println("Destination     : " + s.getDestination());
            System.out.println("Status          : " + s.getStatus());
            System.out.println("Last Updated    : " + s.getLastUpdatedDate());
        } else {
            System.out.println("\n Shipment with Tracking Number '" + trackingNumber + "' not found.");
        }
    }

    private void updateShipmentStatus(Scanner scanner) {
        System.out.print("\nEnter Tracking Number: ");
        String trackingNumber = scanner.nextLine().trim();

        System.out.println("\nAvailable Statuses:");
        for (TrackingStatus status : TrackingStatus.values()) {
            System.out.println(" - " + status.name());
        }

        System.out.print(" Enter new status: ");
        String statusInput = scanner.nextLine().trim();

        try {
            TrackingStatus newStatus = TrackingStatus.valueOf(statusInput.toUpperCase());
            Optional<Shipment> updatedShipment = shipmentService.updateShipmentStatus(trackingNumber, newStatus);

            if (updatedShipment.isPresent()) {
                System.out.println("\n Shipment Status Updated!");
                System.out.println("---------------------------------");
                System.out.println("Tracking Number : " + updatedShipment.get().getTrackingNumber());
                System.out.println("New Status      : " + updatedShipment.get().getStatus());
                System.out.println("Last Updated    : " + updatedShipment.get().getLastUpdatedDate());
            } else {
                System.out.println("\n❌ Shipment with Tracking Number '" + trackingNumber + "' not found.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("\n❌ Invalid status. Please enter a valid status from the list above.");
        }
    }
}
