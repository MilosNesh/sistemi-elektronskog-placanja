package org.example.webshopbackend.service.impl;

import org.example.webshopbackend.domain.AdditionalService;
import org.example.webshopbackend.domain.Reservation;
import org.example.webshopbackend.domain.User;
import org.example.webshopbackend.repository.AdditionalServiceRepository;
import org.example.webshopbackend.repository.InsuranceRepository;
import org.example.webshopbackend.repository.ReservationRepository;
import org.example.webshopbackend.repository.VehicleRepository;
import org.example.webshopbackend.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private InsuranceRepository insuranceRepository;
    @Autowired
    private AdditionalServiceRepository additionalServiceRepository;

    @Override
    public Reservation save(Reservation reservation) {
        if(reservation.getDateTo().before(reservation.getDateFrom()))
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
}
