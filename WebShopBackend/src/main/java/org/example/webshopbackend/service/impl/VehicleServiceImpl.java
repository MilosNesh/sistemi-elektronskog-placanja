package org.example.webshopbackend.service.impl;

import org.example.webshopbackend.domain.Reservation;
import org.example.webshopbackend.domain.Vehicle;
import org.example.webshopbackend.repository.ReservationRepository;
import org.example.webshopbackend.repository.VehicleRepository;
import org.example.webshopbackend.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class VehicleServiceImpl implements VehicleService {
    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private ReservationRepository reservationRepository;

    @Override
    public List<Vehicle> getAll() {
        return vehicleRepository.findAll();
    }

    @Override
    public Vehicle getById(Long id) {
        return vehicleRepository.findById(id).orElse(null);
    }

    @Override
    public List<Vehicle> getAvailable(Date from, Date to) {
        List<Vehicle> vehicles = getAll();
        List<Vehicle> available = new ArrayList<>();

        for (Vehicle vehicle : vehicles) {
            if(isAvailable(vehicle, from, to))
                available.add(vehicle);
        }
        return available;
    }

    private boolean isAvailable(Vehicle vehicle, Date from, Date to) {
        List<Reservation> reservations = reservationRepository.findActual(Date.valueOf(LocalDate.now()));

        for (Reservation reservation : reservations) {
            if (reservation.getVehicle().getId().equals(vehicle.getId()) &&
                    ((from.after(reservation.getDateFrom()) && to.before(reservation.getDateTo()))
                    || (to.after(reservation.getDateFrom()) && to.before(reservation.getDateTo()))
                    || from.equals(reservation.getDateFrom()) || from.equals(reservation.getDateTo())
                    || to.equals(reservation.getDateFrom()) || to.equals(reservation.getDateTo()))) {
                return false;
            }
        }
        return true;
    }
}
