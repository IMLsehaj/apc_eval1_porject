package com.example.demo;

import javax.persistence.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;


    @Email
    @Column(unique = true, nullable = false)
    private String email;

    @Size(min = 10, max = 10, message = "Phone number must be exactly 10 digits")
    @Column(length = 10, nullable = false)
    private String phoneNumber;

    @Column
    private String houseAddress;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    // Default constructor (required by JPA)
    public User() {}

    // Parameterized constructor
    public User(String username, String password, String email,
                String phoneNumber, String houseAddress, UserRole role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.houseAddress = houseAddress;
        this.role = role;
    }

    // Getters and setters


    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getters & Setters

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getHouseAddress() {
        return houseAddress;
    }
    public void setHouseAddress(String houseAddress) {
        this.houseAddress = houseAddress;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private java.util.List<Shipment> shipments;

    public java.util.List<Shipment> getShipments() {
        return shipments;
    }

    public void setShipments(java.util.List<Shipment> shipments) {
        this.shipments = shipments;
    }
}
