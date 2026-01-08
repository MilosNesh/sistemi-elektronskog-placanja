package org.example.bankbackend.controller;

import org.example.bankbackend.domain.CardPaymentRequest;
import org.example.bankbackend.domain.PaymentFormResponse;
import org.example.bankbackend.domain.PaymentResponse;
import org.example.bankbackend.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentFormResponse> getPaymentForm( // Priprema za placanje (otvaranje payment stranice banke)
            @PathVariable Long paymentId
    ){
        PaymentFormResponse response = paymentService.getPaymentForm(paymentId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{paymentId}/pay")
    public ResponseEntity<PaymentResponse> pay(
            @PathVariable Long paymentId, @RequestBody CardPaymentRequest request
            ){
        PaymentResponse response = paymentService.processCardPayment(paymentId, request);
        return ResponseEntity.ok(response);
    }
}
