package org.example.pspbackend.controller;

import org.example.pspbackend.domain.Merchant;
import org.example.pspbackend.domain.PaymentMethod;
import org.example.pspbackend.dto.MerchantDTO;
import org.example.pspbackend.dto.PaymentMethodDTO;
import org.example.pspbackend.service.PaymentMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "payment-method", produces = MediaType.APPLICATION_JSON_VALUE)
public class PaymentMethodController {
    @Autowired
    private PaymentMethodService paymentMethodService;

    @GetMapping("/all")
    public ResponseEntity<List<PaymentMethodDTO>> findAll() {
        List<PaymentMethodDTO> paymentMethodDTOs = paymentMethodService.getAll();
        if(paymentMethodDTOs == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(paymentMethodDTOs);
    }

    @GetMapping("/merchant/{id}")
    public ResponseEntity<List<PaymentMethodDTO>> findByMerchantId(@PathVariable Long id) {
        List<PaymentMethodDTO> paymentMethodDTOs = paymentMethodService.getByMerchantId(id);
        if(paymentMethodDTOs == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(paymentMethodDTOs);
    }
}
