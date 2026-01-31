package org.example.pspbackend.controller;

import org.example.pspbackend.domain.CryptoPayment;
import org.example.pspbackend.domain.Merchant;
import org.example.pspbackend.domain.Transaction;
import org.example.pspbackend.dto.*;
import org.example.pspbackend.repository.CryptoPaymentRepository;
import org.example.pspbackend.service.MerchantService;
import org.example.pspbackend.service.PaymentProviderService;
import org.example.pspbackend.service.PaymentService;
import org.example.pspbackend.service.TransactionService;
import org.example.pspbackend.service.impl.DynamicPaymentProviderRegistryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
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
    @Autowired
    private CryptoPaymentRepository cryptoPaymentRepository;

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
        if (transaction != null && transaction.getStatus() != null) {
            return ResponseEntity.ok(transaction.getMerchant().getSellerUrl()+transaction.getMerchantOrderId());
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/transaction/{transactionId}")
    public ResponseEntity<TransactionDTO> getTransaction(@PathVariable String transactionId){
        Transaction transaction = transactionService.getById(transactionId);
        if(transaction == null) {
            return ResponseEntity.notFound().build();
        }

        TransactionDTO dto = new TransactionDTO(
                transaction.getId(),
                transaction.getMerchant().getMerchantId(),
                transaction.getAmount(),
                transaction.getCurrency(),
                transaction.getMerchantTimestamp(),
                transaction.getMerchantOrderId(),
                transaction.getPspTimestamp()
        );

        return ResponseEntity.ok(dto);
    }

    @PostMapping("/payments/{paymentId}/crypto")
    public ResponseEntity<Map<String, String>> createCryptoPayment(@PathVariable String paymentId,
                                                                   @RequestBody CryptoPaymentRequest request) {
        BigDecimal rsdAmount = request.getAmount();

        BigDecimal btcAmount = paymentService.convertRsdToBtc(rsdAmount); // racuna kurs po fiksnom odnosu
        // BigDecimal btcAmount = paymentService.advancedConvertRsdToBtc(rsdAmount); // racuna kurs u realnom vremenu (koristi API)

        CryptoPayment payment = new CryptoPayment();
        payment.setPaymentId(paymentId);
        payment.setFiatAmount(rsdAmount);
        payment.setBtcAmount(btcAmount);
        payment.setBtcAddress("tb1q48vuqq27jakzh20g5459k4mc24zvhkhfh899dv"); // testnet adresa
        payment.setStatus("PENDING");
        payment.setCreatedAt(LocalDateTime.now());

        cryptoPaymentRepository.save(payment);

        Map<String, String> response = new HashMap<>();
        response.put("btcAddress", payment.getBtcAddress());
        response.put("btcAmount", btcAmount.toPlainString());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/payments/{paymentId}/crypto/status")
    public ResponseEntity<CryptoPaymentStatusDTO> getCryptoStatus(
            @PathVariable String paymentId) {

        CryptoPayment payment = cryptoPaymentRepository.findByPaymentId(paymentId);

        if (payment == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(new CryptoPaymentStatusDTO(
                payment.getStatus(),
                payment.getTxHash()
        ));
    }

    @PostMapping("/payments/{paymentId}/crypto/pay")
    public ResponseEntity<Void> payCryptoPayment(@PathVariable String paymentId) {

        CryptoPayment payment = cryptoPaymentRepository.findByPaymentId(paymentId);

        if (payment == null) {
            return ResponseEntity.notFound().build();
        }

        payment.setStatus("SUCCESS");
        payment.setTxHash("demo-tx-hash-" + System.currentTimeMillis());

        cryptoPaymentRepository.save(payment);

        return ResponseEntity.ok().build();
    }

}
