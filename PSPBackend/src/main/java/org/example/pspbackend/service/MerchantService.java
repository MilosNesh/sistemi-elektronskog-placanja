package org.example.pspbackend.service;

import org.example.pspbackend.domain.Merchant;
import org.example.pspbackend.dto.MerchantDTO;

public interface MerchantService {
    public Merchant getById(Long id);
    public Merchant update(MerchantDTO merchantDTO);
}
