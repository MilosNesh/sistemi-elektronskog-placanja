package org.example.bankbackend.service;

import org.example.bankbackend.domain.*;
import org.example.bankbackend.domain.enums.PaymentStatus;
import org.example.bankbackend.repository.CustomerRepository;
import org.example.bankbackend.repository.MerchantRepository;
import org.example.bankbackend.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
public class PaymentService {
    private final MerchantRepository merchantRepository;
    private final PaymentRepository paymentRepository;
    private final CustomerRepository customerRepository;

    public PaymentService(MerchantRepository merchantRepository, PaymentRepository paymentRepository, CustomerRepository customerRepository) {
        this.merchantRepository = merchantRepository;
        this.paymentRepository = paymentRepository;
        this.customerRepository = customerRepository;
    }

    public PaymentInitResponse initPayment(PaymentInitRequest request){
        Merchant merchant = merchantRepository.findById(request.getMerchantId())
                .orElseThrow(() -> new IllegalArgumentException("Merchant not found"));

        if(!Boolean.TRUE.equals(merchant.getActive())) {
            throw new IllegalStateException("Merchant is not active");
        }

        if(paymentRepository.existsByStan(request.getStan())){
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

        payment = paymentRepository.save(payment);

        return new PaymentInitResponse(payment.getId(), "https://bank-frontend/pay/" + payment.getId());
    }

    public PaymentFormResponse getPaymentForm(Long paymentId){
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found"));

        if(payment.getExpiresAt().isBefore(LocalDateTime.now())){
            payment.setStatus(PaymentStatus.EXPIRED);
            paymentRepository.save(payment);

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
            throw new IllegalStateException("Payment already attempted");
        }

        if (payment.getExpiresAt().isBefore(LocalDateTime.now())) {
            payment.setStatus(PaymentStatus.EXPIRED);
            paymentRepository.save(payment);
            throw new IllegalStateException("Payment expired");
        }

        validatePan(request.getPan());
        validateExpiryDate(request.getExpiryDate());
        validateSecurityCode(request.getSecurityCode());

        Customer customer = payment.getCustomer();
        if(customer.getBalance() < payment.getAmount()){
            payment.setStatus(PaymentStatus.FAILED);
            paymentRepository.save(payment);
            throw new IllegalStateException("Insufficient funds");
        }

        customer.setBalance(customer.getBalance() - payment.getAmount());
        payment.setAttemptCount(1);
        payment.setStatus(PaymentStatus.COMPLETED);

        payment.setGlobalTransactionId(UUID.randomUUID().toString());
        payment.setAcquirerTimestamp(LocalDateTime.now());

        customerRepository.save(customer);
        paymentRepository.save(payment);

        return new PaymentResponse(
                payment.getStatus(),
                payment.getGlobalTransactionId(),
                payment.getAcquirerTimestamp()
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
}
