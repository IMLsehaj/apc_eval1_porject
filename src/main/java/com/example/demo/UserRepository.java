package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
<<<<<<< HEAD
    void deleteByUsername(String username);
=======
>>>>>>> c6b104e9e877563a2e1a1d208f8998f68b682946
}