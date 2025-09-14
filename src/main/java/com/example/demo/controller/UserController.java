package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.User;
import com.example.demo.service.UserService;

@RestController
@RequestMapping("/api/users")
@org.springframework.web.bind.annotation.CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    
    public ResponseEntity<?> createUser(@RequestBody java.util.Map<String, Object> payload) {
        // Map roleCode to UserRole if present
        String username = (String) payload.get("username");

        if (userService.getUserByUsername(username).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("user already exist");
        }

        String password = (String) payload.get("password");
        String email = (String) payload.get("email");
        String phoneNumber = (String) payload.get("phoneNumber");
        String houseAddress = (String) payload.get("houseAddress");
        String roleStr = (String) payload.getOrDefault("role", null);
        String roleCode = (String) payload.getOrDefault("roleCode", "");
        com.example.demo.UserRole role;
        if (roleStr != null) {
            role = com.example.demo.UserRole.valueOf(roleStr);
        } else if (roleCode.equalsIgnoreCase("opaddmin")) {
            role = com.example.demo.UserRole.ADMIN;
        } else if (roleCode.equalsIgnoreCase("manager")) {
            role = com.example.demo.UserRole.MANAGER;
        } else {
            role = com.example.demo.UserRole.CUSTOMER;
        }
        com.example.demo.User user = new com.example.demo.User(username, password, email, phoneNumber, houseAddress, role);
        User created = userService.createUser(user);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/{username}")
    public ResponseEntity<User> getUser(@PathVariable String username) {
        Optional<User> user = userService.getUserByUsername(username);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    // DTO for login
    public static class LoginRequest {
        public String username;
        public String password;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Optional<User> user = userService.getUserByUsername(loginRequest.username);
        if (user.isPresent() && user.get().getPassword().equals(loginRequest.password)) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.status(401).body("Invalid username or password");
        }
    }

    @PutMapping("/{username}")
    public ResponseEntity<?> updateUser(@PathVariable String username,
                                        @RequestBody User user,
                                        @RequestParam String password) {
        Optional<User> existingUserOpt = userService.getUserByUsername(username);
        if (!existingUserOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        User existingUser = existingUserOpt.get();
        if (!existingUser.getPassword().equals(password)) {
            return ResponseEntity.status(403).body("Password incorrect. Update denied.");
        }
        // Only allow updating username, phone, email, house address
        existingUser.setUsername(user.getUsername());
        existingUser.setPhoneNumber(user.getPhoneNumber());
        existingUser.setEmail(user.getEmail());
        existingUser.setHouseAddress(user.getHouseAddress());
        User updated = userService.createUser(existingUser); // save updated user
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<Void> deleteUser(@PathVariable String username) {
        userService.deleteUser(username);
        return ResponseEntity.noContent().build();
    }
}
