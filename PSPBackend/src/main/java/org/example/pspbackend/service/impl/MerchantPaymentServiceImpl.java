package org.example.pspbackend.service.impl;

import org.example.pspbackend.repository.MerchantPaymentMethodRepository;
import org.example.pspbackend.service.MerchantPaymentMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MerchantPaymentServiceImpl implements MerchantPaymentMethodService {
    @Autowired
    private MerchantPaymentMethodRepository merchantPaymentMethodRepository;
}
