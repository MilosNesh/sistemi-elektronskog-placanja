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

    @PostMapping("/success")
    public ResponseEntity<Void> success(@RequestBody String id) {
        id = id.replace("\"", "").trim();
        reservationService.update(UUID.fromString(id), PaymentStatus.PAID);

        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create("https://localhost:4300/payment/success/"+id)).build();
    }

    @PostMapping("/fail")
    public ResponseEntity<Void> fail(@RequestBody String id) {
        id = id.replace("\"", "").trim();
        reservationService.update(UUID.fromString(id), PaymentStatus.NOT_PAID);

        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create("https://localhost:4300/payment/fail/"+id)).build();
    }

    @PostMapping("/error")
    public ResponseEntity<Void> error(@RequestBody String id) {
        id = id.replace("\"", "").trim();
        reservationService.update(UUID.fromString(id), PaymentStatus.ERROR);

        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create("https://localhost:4300/payment/error/"+id)).build();
    }

}
