<<<<<<< HEAD
// package com.example.demo;

// import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.stereotype.Repository;

// import java.util.Optional;

// @Repository
// public interface ShipmentRepository extends JpaRepository<Shipment, Long> {
//     Optional<Shipment> findByTrackingNumber(String trackingNumber);
// }



package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
=======
package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;
>>>>>>> c6b104e9e877563a2e1a1d208f8998f68b682946
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, Long> {
<<<<<<< HEAD

    // Original method (you can keep if needed)
    Optional<Shipment> findByTrackingNumber(String trackingNumber);

    // New method: fetch shipment along with its user to avoid LazyInitializationException
    @Query("SELECT s FROM Shipment s JOIN FETCH s.user WHERE s.trackingNumber = :trackingNumber")
    Optional<Shipment> findByTrackingNumberWithUser(@Param("trackingNumber") String trackingNumber);
}
=======
    Optional<Shipment> findByTrackingNumber(String trackingNumber);
}

>>>>>>> c6b104e9e877563a2e1a1d208f8998f68b682946
