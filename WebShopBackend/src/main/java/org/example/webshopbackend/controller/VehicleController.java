package org.example.webshopbackend.controller;

import org.example.webshopbackend.domain.*;
import org.example.webshopbackend.dto.*;
import org.example.webshopbackend.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDate;
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
    @Autowired
    private CallApiService callApiService;

    @GetMapping("/")
    public ResponseEntity<List<VehicleDTO>> getAll() {
        List<VehicleDTO> vehicles = new ArrayList<>();
        for (Vehicle vehicle : vehicleService.getAll()) {
            vehicles.add(new VehicleDTO(vehicle));
        }

        return ResponseEntity.ok(vehicles);
    }

    @GetMapping("/available/{from}/{to}")
    public ResponseEntity<List<VehicleDTO>> getAvailableVehicles(@PathVariable String from, @PathVariable String to) {
        List<VehicleDTO> vehicles = new ArrayList<>();
        for (Vehicle vehicle : vehicleService.getAvailable(Date.valueOf(from), Date.valueOf(to))) {
            vehicles.add(new VehicleDTO(vehicle));
        }

        return ResponseEntity.ok(vehicles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VehicleDTO> getById(@PathVariable Long id) {
        Vehicle vehicle = vehicleService.getById(id);
        if (vehicle == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new VehicleDTO(vehicle));
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
    public ResponseEntity<String> saveReservation(@RequestBody ReservationDTO reservationDTO) {
        String email = (String) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        User user = userService.getByEmail(email);

        Reservation reservation = new Reservation(reservationDTO);
        reservation.setUser(user);
        reservation = reservationService.save(reservation);
//        if (reservation == null) {
//            return ResponseEntity.badRequest().build();
//        }
        MerchantRequest merchantRequest = new MerchantRequest(1L, "$2a$12$vMJga57Pqt4ZwktqirCGF.MUaVR0Fi4l8EUlSOqu05zUylEwlPTrm", 200, "RSD", "1", Date.valueOf(LocalDate.now()));
        String url = callApiService.callApi(merchantRequest);

        return ResponseEntity.ok(url);
    }

    @GetMapping("/reservation/")
    public ResponseEntity<List<ReservationDTO>> getForUser() {
        String email = (String) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        User user = userService.getByEmail(email);
        List<Reservation> reservations =  reservationService.getForUser(user);
        List<ReservationDTO> reservationDTOs = new ArrayList<>();
        for (Reservation reservation : reservations) {
            reservationDTOs.add(new ReservationDTO(reservation));
        }
        return ResponseEntity.ok(reservationDTOs);
    }
}
