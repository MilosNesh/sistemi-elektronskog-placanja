package org.example.pspbackend.service.impl;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import org.example.pspbackend.dto.PaymentMethodDTO;
import org.example.pspbackend.service.PaymentProviderService;
import org.example.pspbackend.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PayPalPaymentProvider implements PaymentProviderService {
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private APIContext apiContext;

    @Value("${paypal.client-id}")
    private String clientId;

    @Value("${paypal.client-secret}")
    private String clientSecret;

    @Value("${paypal.base-url}")
    private String baseUrl;

    @Override
    public String getSupportedPaymentType() {
        return "PAYPAL";
    }

    @Override
    public String processPayment(String transactionId, PaymentMethodDTO request) {
        org.example.pspbackend.domain.Transaction transaction = transactionService.getById(transactionId);
        if (transaction == null) {
            return null;
        }
        try {
            String successUrl = "https://localhost:8445/payment/"+transactionId+"/success";
            String cancelUrl = "https://localhost:8445/payment/"+transactionId+"/cancel";
            Payment payment = createPayment(transaction.getAmount(), transaction.getCurrency(), "paypal", "sale", "Test payment", cancelUrl, successUrl);
            for(var link: payment.getLinks()){
                if(link.getRel().equals("approval_url")){
                    System.out.println("Link pronadjen: " + link.getHref());
                    return link.getHref();
                }
            }
        }
        catch (PayPalRESTException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Payment createPayment(
            Double total,
            String currency,
            String method,
            String intent,
            String description,
            String cancelUrl,
            String successUrl) throws PayPalRESTException {

        Amount amount = new Amount();
        if(currency.toUpperCase().equals("RSD")){
            amount.setCurrency("USD");
            total = total * 0.010;
        }
//        amount.setCurrency(currency);
        amount.setTotal(String.format(Locale.US, "%.2f", total));

        Transaction transaction = new Transaction();
        transaction.setDescription(description);
        transaction.setAmount(amount);

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod(method);

        Payment payment = new Payment();
        payment.setIntent(intent);
        payment.setPayer(payer);
        payment.setTransactions(transactions);

        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(cancelUrl);
        redirectUrls.setReturnUrl(successUrl);

        payment.setRedirectUrls(redirectUrls);

        return payment.create(apiContext);
    }
}
