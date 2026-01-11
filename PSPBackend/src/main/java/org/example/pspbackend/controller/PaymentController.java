package org.example.pspbackend.controller;

import org.example.pspbackend.domain.Merchant;
import org.example.pspbackend.domain.Transaction;
import org.example.pspbackend.dto.MerchantRequest;
import org.example.pspbackend.dto.PaymentMethodDTO;
import org.example.pspbackend.dto.PaymentResponse;
import org.example.pspbackend.service.MerchantService;
import org.example.pspbackend.service.PaymentProviderService;
import org.example.pspbackend.service.PaymentService;
import org.example.pspbackend.service.TransactionService;
import org.example.pspbackend.service.impl.DynamicPaymentProviderRegistryImpl;
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
    @Autowired
    private DynamicPaymentProviderRegistryImpl registry;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private MerchantService merchantService;

    @PostMapping("/merchant-request")
    public ResponseEntity<String> createPaymentUrl(@RequestBody MerchantRequest request)
    {
        String redirectUrl = paymentService.generatePaymentUrl(request);
        return ResponseEntity.ok(redirectUrl);
    }

    @PostMapping("/{transactionId}/make")
    public ResponseEntity<String> makePayment(
            @PathVariable String transactionId,
            @RequestBody PaymentMethodDTO request
    ) {
        System.out.println("RRRRR: " + request.getType() + " id: " + request.getPaymentMethodId() + " transactionId: " + transactionId);
        String redirectUrl = paymentService.executePayment(transactionId, request);
        System.out.println("RRRRR: " + redirectUrl);
        return ResponseEntity.ok(redirectUrl);
    }

    @PostMapping("/status")
    public ResponseEntity<String> receivePaymentStatus(
            @RequestBody PaymentResponse response
    ) {
        paymentService.sendPaymentStatusToMerchant(response);
        return ResponseEntity.ok("Payment status successfully received");
    }

    @PostMapping("/add-payment-provider")
    public ResponseEntity<String> addProvider(@RequestParam String className) throws Exception {
        Class<?> clazz = Class.forName(className);
        PaymentProviderService provider = (PaymentProviderService) clazz.newInstance();

        registry.addProvider(provider);

        return ResponseEntity.ok( "" + className + " added!");
    }

    @GetMapping("/redirect/{transactionId}")
    public ResponseEntity<String> redirect(@PathVariable String transactionId) {
        Transaction transaction = transactionService.getById(transactionId);
        if (transaction != null && transaction.getGlobalTransactionId() != null) {
            return ResponseEntity.ok(transaction.getMerchant().getSellerUrl()+transaction.getMerchantOrderId());
        }
        return ResponseEntity.notFound().build();
    }
}
