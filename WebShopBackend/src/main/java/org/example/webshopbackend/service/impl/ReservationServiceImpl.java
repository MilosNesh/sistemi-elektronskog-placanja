package org.example.webshopbackend.service.impl;

import org.example.webshopbackend.domain.AdditionalService;
import org.example.webshopbackend.domain.Reservation;
import org.example.webshopbackend.domain.User;
import org.example.webshopbackend.domain.Vehicle;
import org.example.webshopbackend.repository.ReservationRepository;
import org.example.webshopbackend.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    private ReservationRepository reservationRepository;

    @Override
    public Reservation save(Reservation reservation) {
        if(reservation.getDateTo().before(reservation.getDateFrom()))
            return null;

        if(!isAvailable(reservation.getVehicle(), reservation.getDateFrom(), reservation.getDateTo()))
            return null;

        long days = ChronoUnit.DAYS.between(
                reservation.getDateFrom().toLocalDate(),
                reservation.getDateTo().toLocalDate()
        );

        double price = reservation.getVehicle().getPricePerDay() * days;
        if(reservation.getInsurance() != null)
            price += reservation.getInsurance().getPricePerDay() * days;
        if(reservation.getAdditionalServices() != null)
            for(AdditionalService additionalService : reservation.getAdditionalServices()) {
                price += additionalService.getPrice();
            }
        reservation.setTotalPrice(price);
        return reservationRepository.save(reservation);
    }

    @Override
    public Reservation get(Long id) {
        return reservationRepository.findById(id).orElse(null);
    }

    @Override
    public List<Reservation> getForUser(User user) {
        return reservationRepository.findByUser(user);
    }

    private boolean isAvailable(Vehicle vehicle, Date from, Date to) {
        List<Reservation> reservations =
                reservationRepository.findActual(Date.valueOf(LocalDate.now()));

        for (Reservation reservation : reservations) {
            if (reservation.getVehicle().getId().equals(vehicle.getId()) &&
                    from.before(Date.valueOf(reservation.getDateTo().toLocalDate().plusDays(1))) &&
                    to.after(reservation.getDateFrom())) {
                return false;
            }
        }
        return true;
    }


}
