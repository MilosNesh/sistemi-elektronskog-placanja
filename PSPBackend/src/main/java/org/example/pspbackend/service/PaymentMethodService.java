package org.example.pspbackend.service;

import org.example.pspbackend.dto.PaymentMethodDTO;

import java.util.List;

public interface PaymentMethodService {
    public List<PaymentMethodDTO> getAll();
    public List<PaymentMethodDTO> getByMerchantId(Long id);
}
