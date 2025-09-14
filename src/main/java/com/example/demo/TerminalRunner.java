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

        // 🔹 Main shipment operations loop with role-based dashboard
        Optional<User> userOpt = userRepository.findByUsername(loggedInUser);
        if (!userOpt.isPresent()) {
            System.out.println("\n❌ User not found. Please log in again.");
            return;
        }
        User user = userOpt.get();
        UserRole role = user.getRole();
        boolean running = true;
        while (running) {
            printMenuByRole(role);
            String choice = scanner.nextLine().trim();

            switch (role) {
                case ADMIN:
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
                            changeFeesStatus(scanner);
                            break;
                        case "5":
                            updateUserDetails(scanner);
                            break;
                        case "6":
                            System.out.println("\n Logging out...");
                            loggedInUser = null;
                            run();
                            return;
                        case "7":
                            System.out.println("\n Exiting application. Goodbye!");
                            running = false;
                            break;
                        default:
                            System.out.println("\n Invalid choice. Please try again.");
                    }
                    break;
                case MANAGER:
                    switch (choice) {
                        case "1":
                            trackShipment(scanner);
                            break;
                        case "2":
                            updateShipmentStatus(scanner);
                            break;
                        case "3":
                            updateUserDetails(scanner);
                            break;
                        case "4":
                            System.out.println("\n Logging out...");
                            loggedInUser = null;
                            run();
                            return;
                        case "5":
                            System.out.println("\n Exiting application. Goodbye!");
                            running = false;
                            break;
                        default:
                            System.out.println("\n Invalid choice. Please try again.");
                    }
                    break;
                case CUSTOMER:
                default:
                    switch (choice) {
                        case "1":
                            createShipment(scanner);
                            break;
                        case "2":
                            trackShipment(scanner);
                            break;
                        case "3":
                            updateUserDetails(scanner);
                            break;
                        case "4":
                            System.out.println("\n Logging out...");
                            loggedInUser = null;
                            run();
                            return;
                        case "5":
                            System.out.println("\n Exiting application. Goodbye!");
                            running = false;
                            break;
                        default:
                            System.out.println("\n Invalid choice. Please try again.");
                    }
                    break;
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

        System.out.print("Enter your email: ");
        String email = scanner.nextLine().trim();

        System.out.print("Enter your 10-digit phone number: ");
        String phoneNumber = scanner.nextLine().trim();
        while (phoneNumber.length() != 10 || !phoneNumber.matches("\\d{10}")) {
            System.out.print("Invalid phone number. Please enter a 10-digit phone number: ");
            phoneNumber = scanner.nextLine().trim();
        }

        System.out.print("Enter your house address: ");
        String houseAddress = scanner.nextLine().trim();

        System.out.print("Enter role code (leave blank for customer): ");
        String roleCode = scanner.nextLine().trim();
        UserRole role;
        if (roleCode.equalsIgnoreCase("opaddmin")) {
            role = UserRole.ADMIN;
        } else if (roleCode.equalsIgnoreCase("manager")) {
            role = UserRole.MANAGER;
        } else {
            role = UserRole.CUSTOMER;
        }

        User newUser = new User(username, password, email, phoneNumber, houseAddress, role);
        userRepository.save(newUser); // ✅ Stored in SQL
        System.out.println("User registered successfully as " + role + "! Please login.");
    }

    private void login(Scanner scanner) {
        System.out.print("\nEnter username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Enter password: ");
        String password = scanner.nextLine().trim();

        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent() && userOpt.get().getPassword().equals(password)) {
            loggedInUser = username;
            System.out.println(" Login successful! Welcome, " + username + " (Role: " + userOpt.get().getRole() + ")");
        } else {
            System.out.println(" Invalid username or password.");
        }
    }

    // ========================= SHIPMENT SECTION =========================
    private void printMenuByRole(UserRole role) {
        System.out.println("\n--- Main Menu --- (Logged in as: " + loggedInUser + " | Role: " + role + ")");
        switch (role) {
            case ADMIN:
                System.out.println("1  Create a new Shipment");
                System.out.println("2  Track a Shipment");
                System.out.println("3  Update Shipment Status");
                System.out.println("4  Change Fees Status");
                System.out.println("5  Update User Details");
                System.out.println("6  Log Out");
                System.out.println("7  Exit");
                System.out.print(" Enter your choice: ");
                break;
            case MANAGER:
                System.out.println("1  Track a Shipment");
                System.out.println("2  Update Shipment Status");
                System.out.println("3  Update User Details");
                System.out.println("4  Log Out");
                System.out.println("5  Exit");
                System.out.print(" Enter your choice: ");
                break;
            case CUSTOMER:
            default:
                System.out.println("1  Create a new Shipment");
                System.out.println("2  Track a Shipment");
                System.out.println("3  Update User Details");
                System.out.println("4  Log Out");
                System.out.println("5  Exit");
                System.out.print(" Enter your choice: ");
                break;
        }
    }
    // Update user details (email, phone, address, password)
    private void updateUserDetails(Scanner scanner) {
        Optional<User> userOpt = userRepository.findByUsername(loggedInUser);
        if (!userOpt.isPresent()) {
            System.out.println("\n❌ User not found. Please log in again.");
            return;
        }
        User user = userOpt.get();
        System.out.println("\n--- Update User Details ---");
        System.out.println("Current Email: " + user.getEmail());
        System.out.print("Enter new email (leave blank to keep unchanged): ");
        String email = scanner.nextLine().trim();
        if (!email.isEmpty()) user.setEmail(email);

        System.out.println("Current Phone: " + user.getPhoneNumber());
        System.out.print("Enter new 10-digit phone (leave blank to keep unchanged): ");
        String phone = scanner.nextLine().trim();
        if (!phone.isEmpty()) {
            while (phone.length() != 10 || !phone.matches("\\d{10}")) {
                System.out.print("Invalid phone number. Please enter a 10-digit phone number: ");
                phone = scanner.nextLine().trim();
            }
            user.setPhoneNumber(phone);
        }

        System.out.println("Current Address: " + user.getHouseAddress());
        System.out.print("Enter new address (leave blank to keep unchanged): ");
        String address = scanner.nextLine().trim();
        if (!address.isEmpty()) user.setHouseAddress(address);

        System.out.print("Change password? (y/n): ");
        String changePwd = scanner.nextLine().trim();
        if (changePwd.equalsIgnoreCase("y")) {
            System.out.print("Enter current password: ");
            String currentPwd = scanner.nextLine().trim();
            if (!user.getPassword().equals(currentPwd)) {
                System.out.println("Incorrect current password. Password not changed.");
            } else {
                System.out.print("Enter new password: ");
                String newPwd = scanner.nextLine().trim();
                user.setPassword(newPwd);
                System.out.println("Password updated.");
            }
        }
        userRepository.save(user);
        System.out.println("User details updated successfully.");
    }

    private void createShipment(Scanner scanner) {
        System.out.print("\nEnter Origin: ");
        String origin = scanner.nextLine().trim();
        System.out.print("Enter Destination: ");
        String destination = scanner.nextLine().trim();

        Optional<User> userOpt = userRepository.findByUsername(loggedInUser);
        if (!userOpt.isPresent()) {
            System.out.println("\n❌ Could not find user to create shipment.");
            return;
        }
        User user = userOpt.get();

        Shipment shipment = shipmentService.createShipment(origin, destination, user);
        shipment.setFeesPaid(false);
        shipment.setFeesAmount(1000);
        // The user is already set in the service, so we just save the fees info
        shipmentService.updateShipmentStatus(shipment.getTrackingNumber(), shipment.getStatus());

        System.out.println("\n Shipment Created Successfully!");
        System.out.println("---------------------------------");
        System.out.println("Tracking Number : " + shipment.getTrackingNumber());
        System.out.println("Origin          : " + shipment.getOrigin());
        System.out.println("Destination     : " + shipment.getDestination());
        System.out.println("Status          : " + shipment.getStatus());
        System.out.println("Fees to be paid : Rs. 1000");
        System.out.println("Note: You must pay the fees to track your shipment.");
    }

    private void trackShipment(Scanner scanner) {
        System.out.print("\nEnter Tracking Number: ");
        String trackingNumber = scanner.nextLine().trim();

        Optional<Shipment> shipmentOpt = shipmentService.trackShipment(trackingNumber);
        if (shipmentOpt.isPresent()) {
            Shipment s = shipmentOpt.get();
            Optional<User> userOpt = userRepository.findByUsername(loggedInUser);
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                if (user.getRole() == UserRole.CUSTOMER && !loggedInUser.equals(s.getUser().getUsername())) {
                    System.out.println("\n❌ Access denied. You can only track your own shipments.");
                    return;
                }
                if (user.getRole() == UserRole.CUSTOMER && !s.isFeesPaid()) {
                    System.out.println("\n❌ Fees not paid. Please pay the shipment fees to track your shipment.");
                    return;
                }
            }
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
        // Get the logged-in user and check their role
        Optional<User> userOpt = userRepository.findByUsername(loggedInUser);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.getRole() == UserRole.CUSTOMER) {
                System.out.println("\n❌ Access denied. Only ADMIN or MANAGER can update shipment status.");
                return;
            }
        } else {
            System.out.println("\n❌ User not found. Please log in again.");
            return;
        }

        System.out.print("\nEnter Tracking Number: ");
        String trackingNumber = scanner.nextLine().trim();

        Optional<Shipment> shipmentOpt = shipmentService.trackShipment(trackingNumber);
        if (!shipmentOpt.isPresent()) {
            System.out.println("\n❌ Shipment with Tracking Number '" + trackingNumber + "' not found.");
            return;
        }
        Shipment shipment = shipmentOpt.get();
        Optional<User> userOpt2 = userRepository.findByUsername(loggedInUser);
        if (userOpt2.isPresent()) {
            User user = userOpt2.get();
            if (user.getRole() == UserRole.MANAGER && !shipment.isFeesPaid()) {
                System.out.println("\n❌ Fees not paid. Manager cannot update status until fees are paid.");
                return;
            }
        }

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

    // ADMIN ONLY: Change fees status for a shipment
    private void changeFeesStatus(Scanner scanner) {
        System.out.print("\nEnter Tracking Number to change fees status: ");
        String trackingNumber = scanner.nextLine().trim();
        Optional<Shipment> shipmentOpt = shipmentService.trackShipment(trackingNumber);
        if (!shipmentOpt.isPresent()) {
            System.out.println("\n❌ Shipment with Tracking Number '" + trackingNumber + "' not found.");
            return;
        }
        Shipment shipment = shipmentOpt.get();
        System.out.println("Current fees status: " + (shipment.isFeesPaid() ? "PAID" : "NOT PAID"));
        System.out.print("Enter new fees status (paid/not paid): ");
        String status = scanner.nextLine().trim().toLowerCase();
        if (status.equals("paid")) {
            shipment.setFeesPaid(true);
        } else if (status.equals("not paid")) {
            shipment.setFeesPaid(false);
        } else {
            System.out.println("Invalid input. Fees status not changed.");
            return;
        }
        // Save the updated shipment
        shipmentService.updateShipmentStatus(trackingNumber, shipment.getStatus());
        System.out.println("Fees status updated successfully.");
    }
}
