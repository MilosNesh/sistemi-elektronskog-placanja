package org.example.bankbackend.service;

import org.example.bankbackend.domain.*;
import org.example.bankbackend.domain.enums.PaymentStatus;
import org.example.bankbackend.domain.paymentResponse.PaymentResponse;
import org.example.bankbackend.repository.CustomerRepository;
import org.example.bankbackend.repository.MerchantRepository;
import org.example.bankbackend.repository.PaymentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class PaymentService {
    private final MerchantRepository merchantRepository;
    private final PaymentRepository paymentRepository;
    private final CustomerRepository customerRepository;
    private final PspCallbackService pspCallbackService;
    private final Logger logger = LoggerFactory.getLogger(PaymentService.class);

    public PaymentService(MerchantRepository merchantRepository, PaymentRepository paymentRepository, CustomerRepository customerRepository,
                          PspCallbackService pspCallbackService) {
        this.merchantRepository = merchantRepository;
        this.paymentRepository = paymentRepository;
        this.customerRepository = customerRepository;
        this.pspCallbackService = pspCallbackService;
    }

    public PaymentInitResponse initPayment(PaymentInitRequest request, String type){
        Merchant merchant = merchantRepository.findById(request.getMerchantId())
                .orElseThrow(() -> new IllegalArgumentException("Merchant not found"));

        if(!Boolean.TRUE.equals(merchant.getActive())) {
            logger.warn("event=CREATE | merchant={} | result=FAILURE | description=Payment not created, merchant is not active", merchant.getId());
            throw new IllegalStateException("Merchant is not active");
        }

        if(paymentRepository.existsByStan(request.getStan())){
            logger.warn("event=CREATE | merchant={} | result=FAILURE | description=Payment not created, duplicate stan", merchant.getId());
            throw new IllegalArgumentException("Duplicate STAN");
        }

        Payment payment = new Payment();
        payment.setMerchant(merchant);
        payment.setAmount(request.getAmount());
        payment.setCurrency(request.getCurrency());
        payment.setStan(request.getStan());
        payment.setStatus(PaymentStatus.CREATED);
        payment.setAttemptCount(0);

        LocalDateTime now = LocalDateTime.now();
        payment.setCreatedAt(now);
        payment.setExpiresAt(now.plusMinutes(10));
        payment.setPspTimestamp(request.getPspTimestamp());

        if(type.equals("/qr")) {
            payment.setCustomer(customerRepository.getById(1l));
        }

        payment = paymentRepository.save(payment);

        logger.info("event=CREATE | merchant={} | payment={} | result=SUCCESS | description=Payment created", merchant.getId(), payment.getId());

        return new PaymentInitResponse(payment.getId(), "https://localhost:4100/pay/" + payment.getId() + type);
    }

    public PaymentFormResponse getPaymentForm(Long paymentId){
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found"));

        if(payment.getExpiresAt().isBefore(LocalDateTime.now())){
            payment.setStatus(PaymentStatus.EXPIRED);
            paymentRepository.save(payment);

            pspCallbackService.notifyPsp(payment);

            return new PaymentFormResponse(
                    payment.getId(),
                    payment.getAmount(),
                    payment.getCurrency(),
                    true
            );
        }

        if(payment.getAttemptCount() > 0){
            throw new IllegalStateException("Payment already attempted");
        }

        if(payment.getStatus() != PaymentStatus.CREATED){
            throw new IllegalStateException("Invalid payment state");
        }

        payment.setStatus(PaymentStatus.IN_PROGRESS);
        paymentRepository.save(payment);

        return new PaymentFormResponse(
                payment.getId(),
                payment.getAmount(),
                payment.getCurrency(),
                false
        );
    }

    public PaymentResponse processCardPayment(Long paymentId, CardPaymentRequest request){
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found"));

        if (payment.getAttemptCount() > 0) {
            logger.warn("event=PAY | payment = {} | merchant={} | payer={} | channel=CARD | amount={} | currency={} | result=FAILURE | description=Payment already attempted", paymentId, payment.getMerchant().getId(), payment.getCustomer().getId(), payment.getAmount(), payment.getCurrency());
            throw new IllegalStateException("Payment already attempted");
        }

        if (payment.getExpiresAt().isBefore(LocalDateTime.now())) {
            payment.setStatus(PaymentStatus.EXPIRED);
            paymentRepository.save(payment);
            logger.warn("event=PAY | payment = {} | merchant={} | payer={} | channel=CARD | amount={} | currency={} | result=FAILURE | description=Payment expired", paymentId, payment.getMerchant().getId(), payment.getCustomer().getId(), payment.getAmount(), payment.getCurrency());
            pspCallbackService.notifyPsp(payment);
            throw new IllegalStateException("Payment expired");
        }

        validatePan(request.getPan());
        validateExpiryDate(request.getExpiryDate());
        validateSecurityCode(request.getSecurityCode());

        String pan = request.getPan();
        String holder = request.getCardHolderName();

        request.setSecurityCode(null);

        Customer customer = resolveAccount(pan, holder);

        if(customer.getBalance() < payment.getAmount()){
            payment.setStatus(PaymentStatus.FAILED);
            payment.setAttemptCount(1);
            paymentRepository.save(payment);
            logger.warn("event=PAY | payment = {} | merchant={} | payer={} | channel=CARD | amount={} | currency={} | result=FAILURE | description=Not enough money", paymentId, payment.getMerchant().getId(), payment.getCustomer().getId(), payment.getAmount(), payment.getCurrency());
            pspCallbackService.notifyPsp(payment);
            throw new IllegalStateException("Insufficient funds");
        }

        customer.setBalance(customer.getBalance() - payment.getAmount());
        payment.setCustomer(customer);
        payment.setAttemptCount(1);
        payment.setStatus(PaymentStatus.COMPLETED);
        payment.setGlobalTransactionId(UUID.randomUUID().toString());
        payment.setAcquirerTimestamp(LocalDateTime.now());

        customerRepository.save(customer);
        paymentRepository.save(payment);
        logger.info("event=PAY  | payment = {} | merchant={} | payer={} | channel=CARD | amount={} | currency={} | result=SUCCESS | description=Funds are reserved", paymentId, payment.getMerchant().getId(), payment.getCustomer().getId(), payment.getAmount(), payment.getCurrency());
        pspCallbackService.notifyPsp(payment);
        logger.info("event=NOTIFY |  payment = {} | result=SUCCESS | description=PSP is notified", paymentId);

        return new PaymentResponse(
                payment.getStatus(),
                payment.getGlobalTransactionId(),
                payment.getAcquirerTimestamp(),
                payment.getPspTimestamp(),
                payment.getStan(),
                payment.getMerchant().getId()
        );
    }

    public PaymentResponse processQrPayment(Long paymentId, QrPaymentRequest qrRequest) {
        ParsedQrData qr = parseQr(qrRequest.getQrPayload());

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found"));

        // validacija vremena
        if (payment.getExpiresAt().isBefore(LocalDateTime.now())) {
            payment.setStatus(PaymentStatus.EXPIRED);
            paymentRepository.save(payment);
            logger.warn("event=PAY | payment={} | merchant={} | payer={} | channel=QR | amount={} | currency={} | result=FAILURE | description=Payment expired", paymentId, payment.getMerchant().getId(), payment.getCustomer().getId(), payment.getAmount(), payment.getCurrency());
            pspCallbackService.notifyPsp(payment);
            throw new IllegalStateException("Payment expired");
        }

        if (payment.getAttemptCount() > 0) {
            logger.warn("event=PAY | payment={} | merchant={} | payer={} |  channel=QR | amount={} | currency={} | result=FAILURE | description=Payment already attempted", paymentId, payment.getMerchant().getId(), payment.getCustomer().getId(), payment.getAmount(), payment.getCurrency());
            throw new IllegalStateException("Payment already attempted");
        }
        // validacija amount i currency
        if (!payment.getCurrency().equals(qr.currency()) || payment.getAmount() != qr.amount()) {
            logger.warn("event=PAY | payment={} | merchant={} | payer={} | channel=QR | amount={} | currency={} | result=FAILURE | description=QR payment data mismatch", paymentId, payment.getMerchant().getId(), payment.getCustomer().getId(), payment.getAmount(), payment.getCurrency());
            throw new IllegalArgumentException("QR payment data mismatch");
        }

        // validacija merchant account
        Merchant merchant = payment.getMerchant();
        if (!merchant.getAccountNumber().equals(qr.receiverAccount())) {
            throw new IllegalArgumentException("QR merchant account mismatch");
        }

        Customer customer = payment.getCustomer();

        if(customer.getBalance() < payment.getAmount()){
            payment.setStatus(PaymentStatus.FAILED);
            payment.setAttemptCount(1);
            paymentRepository.save(payment);
            logger.warn("event=PAY | payment={} | merchant={} | payer={} | channel=QR | amount={} | currency={} | result=FAILURE | description=Not enough money", paymentId, payment.getMerchant().getId(), payment.getCustomer().getId(), payment.getAmount(), payment.getCurrency());
            pspCallbackService.notifyPsp(payment);
            throw new IllegalStateException("Insufficient funds");
        }

        customer.setBalance(customer.getBalance() - payment.getAmount());
        payment.setCustomer(customer);

        payment.setStatus(PaymentStatus.COMPLETED);
        payment.setGlobalTransactionId(UUID.randomUUID().toString());
        payment.setAcquirerTimestamp(LocalDateTime.now());
        payment.setAttemptCount(1);

        paymentRepository.save(payment);
        customerRepository.save(customer);
        logger.info("event=PAY | payment={} | merchant={} | payer={} | channel=QR | amount={} | currency={} | result=SUCCESS | description=Payment success", paymentId, payment.getMerchant().getId(), payment.getCustomer().getId(), payment.getAmount(), payment.getCurrency());
        pspCallbackService.notifyPsp(payment);
        logger.info("event=NOTIFY |  payment={} | result=SUCCESS | description=PSP is notified.", paymentId);
        return new PaymentResponse(
                payment.getStatus(),
                payment.getGlobalTransactionId(),
                payment.getAcquirerTimestamp(),
                payment.getPspTimestamp(),
                payment.getStan(),
                payment.getMerchant().getId()
        );
    }

    public String generateQr(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found"));

        if (payment.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Payment expired");
        }

        Merchant merchant = payment.getMerchant();

        String amount = String.format("%.2f", payment.getAmount()).replace(".",",");
        logger.info("event=CREATE |  payment={} | result=SUCCESS | description=QR code generated", paymentId);
        return String.join("|",
                "K:PR",
                "V:01",
                "C:1",// znakovni skup (1 - UTF-8)
                "R:" + merchant.getAccountNumber(),
                "N:" + merchant.getName(),
                "I:" + payment.getCurrency() + amount,
                "SF:289",
                "S:Placanje putem QR"
        );
    }

    public ParsedQrData parseQr(String qr) {
        Map<String, String> map = new HashMap<>();

        for (String part : qr.split("\\|")) {
            String[] kv = part.split(":", 2);
            map.put(kv[0], kv[1]);
        }

        if (!"PR".equals(map.get("K"))) {
            throw new IllegalArgumentException("Invalid QR type");
        }

        String receiverAccount = map.get("R");
        String merchantName = map.get("N");

        String iField = map.get("I");
        if (iField == null || iField.length() < 4) {
            throw new IllegalArgumentException("Invalid I field in QR");
        }

        String currency = iField.substring(0, 3); // prva 3 karaktera su valuta
        String amountStr = iField.substring(3).replace(",", "."); // ostatak je amount
        double amount = Double.parseDouble(amountStr);

        return new ParsedQrData(
                currency,
                amount,
                receiverAccount,
                merchantName,
                map.get("S")
        );
    }

    private void validatePan(String pan) {
        if (pan == null || !pan.matches("\\d{13,19}")) {
            throw new IllegalArgumentException("Invalid PAN format");
        }

        int sum = 0;
        boolean alternate = false;

        for (int i = pan.length() - 1; i >= 0; i--) {
            int n = Character.getNumericValue(pan.charAt(i));
            if (alternate) {
                n *= 2;
                if (n > 9) {
                    n -= 9;
                }
            }
            sum += n;
            alternate = !alternate;
        }

        if (sum % 10 != 0) {
            throw new IllegalArgumentException("Invalid PAN (Luhn check failed)");
        }
    }

    private void validateExpiryDate(String expiryDate) {
        if (!expiryDate.matches("\\d{2}/\\d{2}")) {
            throw new IllegalArgumentException("Invalid expiry date format");
        }

        YearMonth exp = YearMonth.parse(expiryDate, DateTimeFormatter.ofPattern("MM/yy"));
        YearMonth now = YearMonth.now();

        if (exp.isBefore(now)) {
            throw new IllegalArgumentException("Card expired");
        }
    }

    private void validateSecurityCode(String code) {
        if (code == null || !code.matches("\\d{3,4}")) {
            throw new IllegalArgumentException("Invalid security code");
        }
    }

    private Customer resolveAccount(String pan, String fullName) {
        String last4 = pan.substring(pan.length() - 4);
        return customerRepository.findByCardLast4AndFullName(last4, fullName)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));
    }
}
