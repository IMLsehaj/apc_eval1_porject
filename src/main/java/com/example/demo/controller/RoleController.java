package com.example.demo.controller;

import com.example.demo.User;
import com.example.demo.UserRepository;
import com.example.demo.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/roles")
@CrossOrigin(origins = "http://localhost:3000")
public class RoleController {

    @Autowired
    private UserRepository userRepository;

    // Get all users with roles
    @GetMapping
    public List<User> getAllUsersWithRoles() {
        return userRepository.findAll();
    }

    // Assign role to a user
    @PostMapping("/assign")
    public ResponseEntity<?> assignRole(@RequestBody RoleRequest request) {
        Optional<User> userOpt = userRepository.findByUsername(request.getUsername());
        if (userOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        User user = userOpt.get();
        user.setRole(UserRole.valueOf(request.getRole().toUpperCase()));
        userRepository.save(user);
        return ResponseEntity.ok(user);
    }

    // Remove role (reset to CUSTOMER by default)
    @DeleteMapping("/remove")
    public ResponseEntity<?> removeRole(@RequestBody RoleRequest request) {
        Optional<User> userOpt = userRepository.findByUsername(request.getUsername());
        if (userOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        User user = userOpt.get();
        user.setRole(UserRole.CUSTOMER); // fallback role
        userRepository.save(user);
        return ResponseEntity.ok(user);
    }

    // DTO for requests
    public static class RoleRequest {
        private String username;
        private String role;

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }

        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
    }
}

