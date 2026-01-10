package org.example.webshopbackend.service;

import org.example.webshopbackend.domain.PaymentStatus;
import org.example.webshopbackend.domain.Reservation;
import org.example.webshopbackend.domain.User;

import java.util.List;
import java.util.UUID;

public interface ReservationService {
    public Reservation save(Reservation reservation);
    public Reservation get(UUID id);
    public List<Reservation> getForUser(User user);
    public Reservation update(UUID id, PaymentStatus paymentStatus);
}
