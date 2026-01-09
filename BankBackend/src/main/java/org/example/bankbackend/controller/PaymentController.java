package org.example.bankbackend.controller;

import org.example.bankbackend.domain.CardPaymentRequest;
import org.example.bankbackend.domain.PaymentFormResponse;
import org.example.bankbackend.domain.QrPaymentRequest;
import org.example.bankbackend.domain.paymentResponse.PaymentResponse;
import org.example.bankbackend.domain.paymentResponse.QrPaymentResponse;
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

    @GetMapping("/{paymentId}/qr")
    public ResponseEntity<QrPaymentResponse> getQr(@PathVariable Long paymentId){
        return ResponseEntity.ok(paymentService.generateQr(paymentId));
    }

    @PostMapping("/{paymentId}/pay")
    public ResponseEntity<PaymentResponse> pay(
            @PathVariable Long paymentId, @RequestBody CardPaymentRequest request
            ){
        PaymentResponse response = paymentService.processCardPayment(paymentId, request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{paymentId}/qr-pay")
    public ResponseEntity<PaymentResponse> payQr(@RequestBody QrPaymentRequest request) {
        return ResponseEntity.ok(paymentService.processQrPayment(request));
    }
}
