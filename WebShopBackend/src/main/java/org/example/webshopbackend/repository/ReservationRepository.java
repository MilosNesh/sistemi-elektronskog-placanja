package org.example.webshopbackend.repository;

import org.example.webshopbackend.domain.Reservation;
import org.example.webshopbackend.domain.User;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReservationRepository extends JpaRepository<Reservation, UUID> {
    List<Reservation> findByUser(User user);

    @Query("SELECT r FROM Reservation r WHERE r.dateTo >= :date")
    List<Reservation> findActual(@Param("date") Date date);
}
