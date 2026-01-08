package org.example.bankbackend.controller;

import org.example.bankbackend.domain.CardPaymentRequest;
import org.example.bankbackend.domain.PaymentFormResponse;
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
    public ResponseEntity<Void> pay( // samo placanje
            @PathVariable Long paymentId,
            @RequestBody CardPaymentRequest request
            ){
        paymentService.processCardPayment(paymentId, request);
        return ResponseEntity.ok().build();
    }
}
