package org.example.webshopbackend.controller;

import org.example.webshopbackend.domain.PaymentStatus;
import org.example.webshopbackend.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping(value = "payment", produces = MediaType.APPLICATION_JSON_VALUE)
public class PaymentController {
    @Autowired
    private ReservationService reservationService;

    @GetMapping("/success/{id}")
    public ResponseEntity<Void> success(@PathVariable UUID id) {
        reservationService.update(id, PaymentStatus.PAID);

        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create("https://localhost:4300/payment/success/"+id)).build();
    }

    @GetMapping("/fail/{id}")
    public ResponseEntity<Void> fail(@PathVariable UUID id) {
        reservationService.update(id, PaymentStatus.NOT_PAID);
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create("https://localhost:4300/payment/fail/"+id)).build();
    }

    @GetMapping("/error/{id}")
    public ResponseEntity<Void> error(@PathVariable UUID id) {
        reservationService.update(id, PaymentStatus.ERROR);
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create("https://localhost:4300/payment/error/"+id)).build();
    }

}
