package org.example.pspbackend.service.impl;

import org.example.pspbackend.domain.PaymentMethod;
import org.example.pspbackend.dto.PaymentMethodDTO;
import org.example.pspbackend.repository.PaymentMethodRepository;
import org.example.pspbackend.service.PaymentMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentMethodServiceImpl implements PaymentMethodService {
    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    public List<PaymentMethodDTO> getAll() {
        return paymentMethodRepository.findAll().stream()
                .map(pm -> new PaymentMethodDTO(
                        pm.getPaymentMethodId(),
                        pm.getType(),
                        pm.getImage(),
                        pm.getDescription()
                ))
                .toList();
    }

    public List<PaymentMethodDTO> getByMerchantId(Long id){
        List<PaymentMethod> methods = paymentMethodRepository.findByMerchantId(id);

        return methods.stream()
                .map(pm -> new PaymentMethodDTO(
                        pm.getPaymentMethodId(),
                        pm.getType(),
                        pm.getImage(),
                        pm.getDescription()
                ))
                .toList();
    }
}
