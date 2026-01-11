package org.example.pspbackend.service.impl;

import org.example.pspbackend.domain.Merchant;
import org.example.pspbackend.domain.MerchantPaymentMethod;
import org.example.pspbackend.domain.PaymentMethod;
import org.example.pspbackend.dto.LoginDetailsDTO;
import org.example.pspbackend.dto.MerchantDTO;
import org.example.pspbackend.dto.MerchantPaymentMethodDTO;
import org.example.pspbackend.repository.MerchantRepository;
import org.example.pspbackend.repository.PaymentMethodRepository;
import org.example.pspbackend.security.PasswordHasher;
import org.example.pspbackend.service.MerchantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class MerchantServiceImpl implements MerchantService {
    @Autowired
    private MerchantRepository merchantRepository;
    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    public Merchant getById(Long id){
        return merchantRepository.findById(id).orElse(null);
    }

    public Merchant update(MerchantDTO merchantDTO){
        Merchant merchant = merchantRepository.findById(merchantDTO.getMerchantId())
                .orElseThrow(() -> new RuntimeException("Merchant not found"));

        merchant.setSuccessUrl(merchantDTO.getSuccessUrl());
        merchant.setFailedUrl(merchantDTO.getFailedUrl());
        merchant.setErrorUrl(merchantDTO.getErrorUrl());
        merchantRepository.save(merchant);

        Map<Long, MerchantPaymentMethod> existing = merchant.getMerchantPaymentMethods()
                .stream()
                .collect(Collectors.toMap(
                        MerchantPaymentMethod::getMerchantPaymentMethodId,
                        Function.identity()
                ));

        List<MerchantPaymentMethod> updated = new ArrayList<>();

        for (MerchantPaymentMethodDTO pmDto : merchantDTO.getMerchantPaymentMethods()) {

            MerchantPaymentMethod mpm;

            if (pmDto.getMerchantPaymentMethodId() != null && existing.containsKey(pmDto.getMerchantPaymentMethodId())) {
                mpm = existing.get(pmDto.getMerchantPaymentMethodId());
            } else {
                mpm = new MerchantPaymentMethod();
                mpm.setMerchant(merchant);
            }
            mpm.setEnabled(pmDto.getEnabled());

            PaymentMethod paymentMethod = paymentMethodRepository
                    .findById(pmDto.getPaymentMethodId())
                    .orElseThrow(() -> new RuntimeException("PaymentMethod not found"));

            mpm.setPaymentMethod(paymentMethod);

            updated.add(mpm);
        }
        merchant.getMerchantPaymentMethods().clear();
        merchant.getMerchantPaymentMethods().addAll(updated);

        return merchantRepository.save(merchant);
    }

    public Merchant getByEmail(String email){
        return merchantRepository.findByMerchantEmail(email);
    }

    public boolean login(LoginDetailsDTO loginDetailsDTO){
        Merchant merchant = getByEmail(loginDetailsDTO.getEmail());
        return PasswordHasher.verifyPassword(loginDetailsDTO.getPassword(), merchant.getMerchantPassword());
    }
}
