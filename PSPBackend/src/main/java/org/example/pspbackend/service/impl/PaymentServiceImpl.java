package org.example.pspbackend.service.impl;

import org.example.pspbackend.repository.PaymentMethodRepository;
import org.example.pspbackend.service.PaymentMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentMethodService {
    @Autowired
    private PaymentMethodRepository merchantPaymentMethodRepository;
}
