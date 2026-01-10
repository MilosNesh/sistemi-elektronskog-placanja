package org.example.pspbackend.service.impl;

import org.example.pspbackend.service.PaymentProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class DynamicPaymentProviderRegistryImpl {
    private final Map<String, PaymentProviderService> providers = new ConcurrentHashMap<>();

    @Autowired
    public DynamicPaymentProviderRegistryImpl(List<PaymentProviderService> allProviders) {
        for (PaymentProviderService provider : allProviders) {
            String type = provider.getSupportedPaymentType();
            providers.put(type.toUpperCase(), provider);
            System.out.println("Registered provider for type: " + type);
        }
    }

    public PaymentProviderService getProvider(String paymentType) {
        PaymentProviderService provider = providers.get(paymentType.toUpperCase());

        if (provider == null) {
            throw new IllegalArgumentException("No provider for: " + paymentType);
        }

        return provider;
    }

    public void addProvider(PaymentProviderService newProvider) {
        String type = newProvider.getSupportedPaymentType();
        providers.put(type.toUpperCase(), newProvider);
        System.out.println("Dynamically added provider for: " + type);
    }
}
