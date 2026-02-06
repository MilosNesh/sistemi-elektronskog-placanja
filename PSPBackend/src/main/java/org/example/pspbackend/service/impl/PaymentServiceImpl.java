package org.example.pspbackend.service.impl;

import com.paypal.base.rest.APIContext;
import org.example.pspbackend.component.StanGenerator;
import org.example.pspbackend.domain.Merchant;
import org.example.pspbackend.domain.Transaction;
import org.example.pspbackend.dto.MerchantRequest;
import org.example.pspbackend.dto.PaymentMethodDTO;
import org.example.pspbackend.repository.MerchantRepository;
import org.example.pspbackend.dto.PaymentResponse;
import org.example.pspbackend.repository.TransactionRepository;
import org.example.pspbackend.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    private CryptoRateService cryptoRateService;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private MerchantRepository merchantRepository;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private DynamicPaymentProviderRegistryImpl registry;
    @Autowired
    private CallMerchantApiService callMerchantApiService;
    @Autowired
    private MerchantServiceImpl merchantService;
    @Autowired
    private StanGenerator stanGenerator;
    private final List<PaymentProviderService> providers;

    private final Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);
    @Autowired
    private APIContext apiContext;
    public PaymentServiceImpl(List<PaymentProviderService> providers) {
        this.providers = providers;
    }

    public String generatePaymentUrl(MerchantRequest merchantRequest){
        if(merchantRequest.getMerchantId() == null){
            logger.info("event=CREATE | user={} | result=FAILURE | description=Transaction not created, user not exist", merchantRequest.getMerchantId());
            return null;
        }

        Merchant merchant = merchantService.getById(merchantRequest.getMerchantId());
        if(merchant == null || !merchantRequest.getMerchantPassword().equals(merchant.getMerchantPassword())){
            logger.info("event=CREATE | user={} | result=FAILURE | description=Transaction not created, invalid user password", merchantRequest.getMerchantId());
            return null;
        }

        LocalDateTime pspTimestamp = LocalDateTime.now();

        Transaction transaction = new Transaction(
                merchant,
                merchantRequest.getAmount(),
                merchantRequest.getCurrency(),
                merchantRequest.getMerchantTimestamp(),
                merchantRequest.getMerchantOrderId(),
                pspTimestamp,
                stanGenerator.generateStan()

        );
        transactionService.save(transaction);

        logger.info("event=CREATE | user={} | transaction={} | result=SUCCESS | description=Transaction created", merchant.getMerchantEmail(), transaction.getId());
        return "https://localhost:4200/payment/" + transaction.getId() + "/" + merchant.getMerchantId();
    }

    public String executePayment(String transactionId, PaymentMethodDTO request) {
        PaymentProviderService provider = registry.getProvider(request.getType());

        return provider.processPayment(transactionId, request);
    }

//    public BigDecimal convertRsdToEth(BigDecimal rsd) {
//        BigDecimal ethRate = new BigDecimal("0.00000433"); // skontaj broj
//        return rsd.multiply(ethRate);
//    }
//
//    public BigDecimal advancedConvertRsdToEth(BigDecimal fiatAmount) {
//        return cryptoRateService.convertRsdToEth(fiatAmount);
//    }

    public void sendPaymentStatusToMerchant(PaymentResponse paymentResponse){

        System.out.println("Prvi:  " + paymentResponse.getPspTimestamp());
        LocalDateTime pspTimestamp = LocalDateTime.parse(paymentResponse.getPspTimestamp());

        Transaction transaction = transactionService.getByStan(paymentResponse.getStan());
//        System.out.println("Iz baze: " + transaction1.getStan() + " | " + transaction1.getMerchant().getMerchantId() + " | " + transaction1.getPspTimestamp());
//        Transaction transaction = transactionService.get(paymentResponse.getStan(), paymentResponse.getMerchantId(), pspTimestamp);
        System.out.println("Iz responsa: " + paymentResponse.getStan() + " | " + paymentResponse.getMerchantId() + " | " + pspTimestamp);

        System.out.println("Drugi: " + pspTimestamp);
        if(transaction == null){
            return;
        }

        transaction.setGlobalTransactionId(paymentResponse.getGlobalTransactionId());
        transaction.setAcquirerTimestamp(paymentResponse.getAcquirerTimestamp());
        transaction.setStatus(paymentResponse.getStatus());
        transactionService.save(transaction);

        logger.info("event=UPDATE | user={} | transaction={} | result=SUCCESS | description=Transaction updated", transaction.getMerchant().getMerchantEmail(), transaction.getId());

        Merchant merchant = merchantService.getById(paymentResponse.getMerchantId());
        if(merchant == null){
            return;
        }

        switch (paymentResponse.getStatus()){
            case "COMPLETED":
                callMerchantApiService.notifyPaymentSuccess(merchant.getSuccessUrl(), transaction.getMerchantOrderId());
                logger.info("event=PAY | user={} | transaction={} | result=SUCCESS | description=Transaction payed", merchant.getMerchantEmail(), transaction.getId());
                break;
            case "FAILED":
                callMerchantApiService.notifyPaymentFailed(merchant.getFailedUrl(), transaction.getMerchantOrderId());
                logger.warn("event=PAY | user={} | transaction={} | result=FAILED | description=Transaction not payed", merchant.getMerchantEmail(), transaction.getId());
                break;
            case "EXPIRED":
                callMerchantApiService.notifyPaymentError(merchant.getErrorUrl(), transaction.getMerchantOrderId());
                logger.warn("event=PAY | user={} | transaction={} | result=EXPIRED | description=Transaction not payed", merchant.getMerchantEmail(), transaction.getId());
                break;
            default:
                callMerchantApiService.notifyPaymentError(merchant.getErrorUrl(), transaction.getMerchantOrderId());
                logger.warn("event=PAY | user={} | transaction={} | result=ERROR | description=Transaction not payed", merchant.getMerchantEmail(), transaction.getId());

        }
        logger.info("event=SEND_STATUS | user={} | transaction={} | result=SUCCESS | description=Merchant notified", merchant.getMerchantEmail(), transaction.getId());

    }

    @Override
    public String sendPaymentStatusToMerchant(String transactionId, String paymentId, String payerId, String status) {
        Transaction transaction = transactionService.getById(transactionId);
        if(transaction == null){
            return null;
        }
        transaction.setGlobalTransactionId(paymentId);
        transaction.setStatus(status);
        transactionService.save(transaction);

        logger.info("event=UPDATE | user={} | transaction={} | result=SUCCESS | description=Transaction updated.", transaction.getMerchant().getMerchantEmail(), transaction.getId());

        Merchant merchant = transaction.getMerchant();
        if(merchant == null){
            return null;
        }
        if(status.equals("COMPLETED")) {
            callMerchantApiService.notifyPaymentSuccess(merchant.getSuccessUrl(), transaction.getMerchantOrderId());
            logger.info("event=PAY | user={} | transaction={} | result=SUCCESS | description=Transaction payed.", merchant.getMerchantEmail(), transaction.getId());
        }
        else {
            callMerchantApiService.notifyPaymentFailed(merchant.getFailedUrl(), transaction.getMerchantOrderId());
            logger.warn("event=PAY | user={} | transaction={} | result=FAILURE | description=Transaction payed", merchant.getMerchantEmail(), transaction.getId());
        }
        logger.info("event=SEND_STATUS | user={} | transaction={} | result=SUCCESS | description=Merchant notified.", merchant.getMerchantEmail(), transaction.getId());
        return "https://localhost:4200/payment/"+transactionId+"/"+merchant.getMerchantId()+"/";
    }
}
