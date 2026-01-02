package org.example.webshopbackend.service;

import org.example.webshopbackend.domain.Reservation;
import org.example.webshopbackend.domain.User;

import java.util.List;

public interface ReservationService {
    public Reservation save(Reservation reservation);
    public Reservation get(Long id);
    public List<Reservation> getForUser(User user);
}
