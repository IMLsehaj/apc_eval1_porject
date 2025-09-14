package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/shipments")
@CrossOrigin(origins = "http://localhost:3000")
public class ShipmentController {

    private final ShipmentService shipmentService;
    private final UserRepository userRepository;

    // Constructor-based dependency injection is recommended
    @Autowired
    public ShipmentController(ShipmentService shipmentService, UserRepository userRepository) {
        this.shipmentService = shipmentService;
        this.userRepository = userRepository;
    }

    /**
     * Endpoint to create a new shipment.
     * The request body should contain the origin and destination.
     * param shipment The shipment data from the request body.
     * return The created shipment with its new tracking number.
     */
    @PostMapping("/createshipment")
    public ResponseEntity<?> createShipment(@RequestBody Shipment shipment, @RequestParam String username) {
        Optional <User> userOpt = userRepository.findByUsername(username);
        if (!userOpt.isPresent()) {
            return new ResponseEntity<>("User not found: " + username, HttpStatus.NOT_FOUND);
        }
        User user = userOpt.get();
        Shipment createdShipment = shipmentService.createShipment(shipment.getOrigin(), shipment.getDestination(), user);
        return new ResponseEntity<>(createdShipment, HttpStatus.CREATED);
    }

    /**
     * Endpoint to get a shipment by its tracking number.
     * trackingNumber The tracking number of the shipment.
     *  The shipment if found, otherwise a 404 Not Found response.
     */

    // 
    



@GetMapping("/track/{trackingNumber}")
public ResponseEntity<?> getShipmentByTrackingNumber(@PathVariable String trackingNumber,
                                                     @RequestParam String username) {
    // 1️⃣ Get the logged-in user
    Optional<User> userOpt = userRepository.findByUsername(username);
    if (!userOpt.isPresent()) {
        return new ResponseEntity<>("User not found.", HttpStatus.UNAUTHORIZED);
    }
    User user = userOpt.get();

    // 2️⃣ Get shipment with its user fully loaded
    Optional<Shipment> shipmentOpt = shipmentService.trackShipmentWithUser(trackingNumber);
    if (!shipmentOpt.isPresent()) {
        return new ResponseEntity<>("Shipment not found.", HttpStatus.NOT_FOUND);
    }
    Shipment shipment = shipmentOpt.get();

    // 3️⃣ Ensure shipment has a valid user
    if (shipment.getUser() == null || shipment.getUser().getUsername() == null) {
        return new ResponseEntity<>("Shipment has no valid user linked.", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // 4️⃣ Role-based access
    switch (user.getRole()) {
        case ADMIN:
            return new ResponseEntity<>(shipment, HttpStatus.OK);

        case MANAGER:
            if (!shipment.isFeesPaid()) {
                return new ResponseEntity<>("Fees not paid. Manager cannot track this shipment.", HttpStatus.FORBIDDEN);
            }
            return new ResponseEntity<>(shipment, HttpStatus.OK);

        case CUSTOMER:
            // Customer can only track their own shipment
            if (!user.getUsername().equals(shipment.getUser().getUsername())) {
                return new ResponseEntity<>("Access denied. You can only track your own shipments.", HttpStatus.FORBIDDEN);
            }
            if (!shipment.isFeesPaid()) {
                return new ResponseEntity<>("Fees not paid. Please pay the shipment fees to track your shipment.", HttpStatus.FORBIDDEN);
            }
            return new ResponseEntity<>(shipment, HttpStatus.OK);

        default:
            return new ResponseEntity<>("Invalid user role.", HttpStatus.FORBIDDEN);
    }
}


    /**
     * Endpoint for admin to change the fees status of a shipment.
     */
    @PutMapping("/track/{trackingNumber}/fees")
    public ResponseEntity<?> changeFeesStatus(@PathVariable String trackingNumber,
                                              @RequestParam boolean paid,
                                              @RequestParam String username) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (!userOpt.isPresent()) {
            return new ResponseEntity<>("User not found."+username, HttpStatus.UNAUTHORIZED);
        }
        User user = userOpt.get();
        if (user.getRole() != UserRole.ADMIN) {
            return new ResponseEntity<>("Access denied. Only ADMIN can change fees status.", HttpStatus.FORBIDDEN);
        }
        
        Optional<Shipment> updatedShipment = shipmentService.updateFeesStatus(trackingNumber, paid);

        return updatedShipment.map(s -> new ResponseEntity<>(s, HttpStatus.OK))
                              .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Endpoint to update shipment status.
     * Only ADMIN and MANAGER can update shipment status.
     * URL: /api/shipments/track/{trackingNumber}/status?username={username}&status={status}
     */
    @PutMapping("/track/{trackingNumber}/status")
    public ResponseEntity<?> updateShipmentStatus(@PathVariable String trackingNumber,
                                                  @RequestParam String username,
                                                  @RequestParam String status) {
        System.out.println("DEBUG: Received status update request - trackingNumber: " + trackingNumber + 
                          ", username: " + username + ", status: " + status);
        
        // 1️⃣ Validate user
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (!userOpt.isPresent()) {
            System.out.println("DEBUG: User not found: " + username);
            return new ResponseEntity<>("User not found: " + username, HttpStatus.UNAUTHORIZED);
        }
        User user = userOpt.get();
        System.out.println("DEBUG: User found - " + user.getUsername() + " with role: " + user.getRole());

        // 2️⃣ Check user permissions (only ADMIN and MANAGER can update status)
        if (user.getRole() != UserRole.ADMIN && user.getRole() != UserRole.MANAGER) {
            return new ResponseEntity<>("Access denied. Only ADMIN and MANAGER can update shipment status.", HttpStatus.FORBIDDEN);
        }

        // 3️⃣ Validate status parameter
        TrackingStatus trackingStatus;
        try {
            trackingStatus = TrackingStatus.valueOf(status.toUpperCase());
            System.out.println("DEBUG: Status validated: " + trackingStatus);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Invalid status: " + status + 
                                      ". Valid statuses are: PENDING, IN_TRANSIT, OUT_FOR_DELIVERY, DELIVERED, CANCELLED, EXCEPTION", 
                                      HttpStatus.BAD_REQUEST);
        }

        // 4️⃣ Update shipment status
        Optional<Shipment> updatedShipment = shipmentService.updateShipmentStatus(trackingNumber, trackingStatus);
        
        if (updatedShipment.isPresent()) {
            System.out.println("DEBUG: Shipment status updated successfully");
            return new ResponseEntity<>(updatedShipment.get(), HttpStatus.OK);
        } else {
            System.out.println("DEBUG: Shipment not found with tracking number: " + trackingNumber);
            return new ResponseEntity<>("Shipment not found with tracking number: " + trackingNumber, HttpStatus.NOT_FOUND);
        }
    }


}
