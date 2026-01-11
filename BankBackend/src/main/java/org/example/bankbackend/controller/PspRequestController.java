package org.example.bankbackend.controller;

import org.example.bankbackend.domain.PaymentInitRequest;
import org.example.bankbackend.domain.PaymentInitResponse;
import org.example.bankbackend.service.PaymentService;
import org.example.bankbackend.service.PspAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PspRequestController {
    private final PspAuthService pspAuthService;
    private final PaymentService paymentService;

    public PspRequestController(PspAuthService pspAuthService, PaymentService paymentService) {
        this.pspAuthService = pspAuthService;
        this.paymentService = paymentService;
    }

    @PostMapping("/init")
    public ResponseEntity<PaymentInitResponse> initPayment(
            @RequestBody PaymentInitRequest request,
            @RequestHeader("X-PSP-SIGNATURE") String signature
            ){
        System.out.println("HMAC: " + signature);
        pspAuthService.validateRequest(request, signature);
        PaymentInitResponse response = paymentService.initPayment(request);
        return ResponseEntity.ok(response);
    }
}
