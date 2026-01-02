package org.example.webshopbackend.controller;

import org.example.webshopbackend.domain.*;
import org.example.webshopbackend.dto.AdditionalServiceDTO;
import org.example.webshopbackend.dto.InsuranceDTO;
import org.example.webshopbackend.dto.ReservationDTO;
import org.example.webshopbackend.dto.VehicleDTO;
import org.example.webshopbackend.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "vehicle", produces = MediaType.APPLICATION_JSON_VALUE)
public class VehicleController {
    @Autowired
    private VehicleService vehicleService;
    @Autowired
    private InsuranceService insuranceService;
    @Autowired
    private AdditionalServiceService additionalServiceService;
    @Autowired
    private ReservationService reservationService;
    @Autowired
    private UserService userService;

    @GetMapping("/")
    public ResponseEntity<List<VehicleDTO>> getAll() {
        List<VehicleDTO> vehicles = new ArrayList<>();
        for (Vehicle vehicle : vehicleService.getAll()) {
            vehicles.add(new VehicleDTO(vehicle));
        }

        return ResponseEntity.ok(vehicles);
    }

    @GetMapping("/insurance/")
    public ResponseEntity<List<InsuranceDTO>> getAllInsurances() {
        List<InsuranceDTO> insurances = new ArrayList<>();
        for (Insurance insurance : insuranceService.getAll()) {
            insurances.add(new InsuranceDTO(insurance));
        }

        return ResponseEntity.ok(insurances);
    }

    @GetMapping("/additional-service/")
    public ResponseEntity<List<AdditionalServiceDTO>> getAllAdditionalServices() {
        List<AdditionalServiceDTO> additionalServices = new ArrayList<>();
        for (AdditionalService additionalService : additionalServiceService.getAll()) {
            additionalServices.add(new AdditionalServiceDTO(additionalService));
        }

        return ResponseEntity.ok(additionalServices);
    }

    @PostMapping("/reservation/")
    public ResponseEntity<ReservationDTO> saveReservation(@RequestBody ReservationDTO reservationDTO) {
        String email = (String) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        User user = userService.getByEmail(email);

        Reservation reservation = new Reservation(reservationDTO);
        reservation.setUser(user);
        reservation = reservationService.save(reservation);
        if (reservation == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(new ReservationDTO(reservation));
    }
}
