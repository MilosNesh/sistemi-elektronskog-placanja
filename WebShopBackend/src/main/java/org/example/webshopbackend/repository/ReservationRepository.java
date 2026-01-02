package org.example.webshopbackend.repository;

import org.example.webshopbackend.domain.Reservation;
import org.example.webshopbackend.domain.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByUser(User user);
}
