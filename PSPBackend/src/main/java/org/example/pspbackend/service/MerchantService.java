package org.example.pspbackend.service;

import org.example.pspbackend.domain.Merchant;
import org.example.pspbackend.dto.LoginDetailsDTO;
import org.example.pspbackend.dto.MerchantDTO;

public interface MerchantService {
    public Merchant getById(Long id);
    public Merchant update(MerchantDTO merchantDTO);
    public Merchant getByEmail(String email);
    public boolean login(LoginDetailsDTO loginDetailsDTO);
}
