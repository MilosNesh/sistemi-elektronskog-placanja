package org.example.pspbackend.controller;

import org.example.pspbackend.domain.ETHPayment;
import org.example.pspbackend.domain.Transaction;
import org.example.pspbackend.dto.*;
import org.example.pspbackend.repository.EthPaymentRepository;
import org.example.pspbackend.service.*;
import org.example.pspbackend.service.impl.DynamicPaymentProviderRegistryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.time.LocalDateTime;

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
    private EthPaymentRepository ethPaymentRepository;
    @Autowired
    private EthPaymentService ethPaymentService;

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

    @PostMapping("/eth/{transactionId}/create")
    public ResponseEntity<?> createEthPayment(
            @PathVariable String transactionId,
            @RequestBody CryptoPaymentRequest request
    ) {
        BigDecimal rsdAmount = request.getAmount();
        BigDecimal ethAmount = paymentService.convertRsdToEth(rsdAmount); // pretvaranje preko fiksnog odnosa
        // BigDecimal ethAmount = paymentService.advancedConvertRsdToEth(rsdAmount); // pretvaranje preko odnosa kursa u realnom vremenu

        System.out.println("ETH amount: " + ethAmount);

        ETHPayment payment = new ETHPayment();
        payment.setPaymentId(transactionId);
        payment.setFiatAmount(rsdAmount);
        payment.setEthAmount(ethAmount);
        payment.setFromAddress("0xf94f22Fa95660100FAaecC429c9C8d3B641Ec262");
        payment.setToAddress("0xEbC45d552E0947cFf740426A25fD1A3f50CaCf7e");
        payment.setStatus("CREATED");
        payment.setCreatedAt(LocalDateTime.now());

        ethPaymentRepository.save(payment);

        return ResponseEntity.ok(payment);
    }

    @GetMapping("/eth/balance")
    public ResponseEntity<String> balance() throws Exception {
        BigInteger wei = ethPaymentService.getBalance();
        return ResponseEntity.ok(Convert.fromWei(new BigDecimal(wei), Convert.Unit.ETHER).toPlainString());
    }

    @PostMapping("/eth/send")
    public ResponseEntity<String> sendEth(@RequestBody EthPaymentRequest request) {
        try {
            String txHash = ethPaymentService.sendEth(request.getToAddress(), request.getAmount());
            return ResponseEntity.ok(txHash);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error sending ETH: " + e.getMessage());
        }
    }

    @GetMapping("/{transactionId}/success")
    public ResponseEntity<?> success(
            @RequestParam("paymentId") String paymentId,
            @RequestParam("PayerID") String payerId,
            @RequestParam(value = "token", required = false) String token,
            @PathVariable String transactionId
    ) {
        String link = paymentService.sendPaymentStatusToMerchant(transactionId, paymentId, payerId, "COMPLETED");
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(link)).build();
    }

    @GetMapping("/{transactionId}/cancel")
    public ResponseEntity<?> cacnel(
            @RequestParam(value = "paymentId", required = false) String paymentId,
            @RequestParam(value = "PayerID", required = false) String payerId,
            @RequestParam(value = "token", required = false) String token,
            @PathVariable String transactionId
    ) {
        String link = paymentService.sendPaymentStatusToMerchant(transactionId, paymentId, payerId, "CANCEL");
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(link)).build();
    }


}
