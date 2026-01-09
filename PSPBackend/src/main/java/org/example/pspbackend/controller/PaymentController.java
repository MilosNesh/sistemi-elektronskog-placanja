package org.example.pspbackend.controller;

import org.example.pspbackend.dto.MerchantRequest;
import org.example.pspbackend.dto.PaymentMethodDTO;
import org.example.pspbackend.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "payment", produces = MediaType.APPLICATION_JSON_VALUE)
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @PostMapping("/merchant-request")
    public ResponseEntity<String> createPayment(@RequestBody MerchantRequest request) {

        String redirectUrl = paymentService.generatePaymentUrl(request);

        return ResponseEntity.ok(redirectUrl);
    }

    @PostMapping("/{transactionId}/make")
    public ResponseEntity<String> makePayment(
            @PathVariable String transactionId,
            @RequestBody PaymentMethodDTO request
    ) {
        String redirectUrl = paymentService.executePayment(transactionId, request);

        // Vrati redirect URL frontend-u
        return ResponseEntity.ok(redirectUrl);
    }
}
