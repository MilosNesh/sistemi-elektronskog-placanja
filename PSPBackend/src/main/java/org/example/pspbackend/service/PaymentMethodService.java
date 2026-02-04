package org.example.pspbackend.service;

import org.example.pspbackend.domain.PaymentMethod;
import org.example.pspbackend.dto.PaymentMethodDTO;

import java.util.List;

public interface PaymentMethodService {
    public List<PaymentMethodDTO> getAll();
    public List<PaymentMethodDTO> getAllAvailable();
    public List<PaymentMethodDTO> getByMerchantId(Long id);
    public PaymentMethodDTO save(PaymentMethodDTO paymentMethodDTO);
    public PaymentMethod setMethodAvailability(Long paymentMethodId, Boolean isAvailable);
}
