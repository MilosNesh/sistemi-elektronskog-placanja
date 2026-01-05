package org.example.pspbackend.service.impl;

import org.example.pspbackend.domain.Merchant;
import org.example.pspbackend.dto.LoginDetailsDTO;
import org.example.pspbackend.dto.MerchantDTO;
import org.example.pspbackend.repository.MerchantRepository;
import org.example.pspbackend.security.PasswordHasher;
import org.example.pspbackend.service.MerchantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MerchantServiceImpl implements MerchantService {
    @Autowired
    private MerchantRepository merchantRepository;

    public Merchant getById(Long id){
        return merchantRepository.findById(id).orElse(null);
    }

    public Merchant update(MerchantDTO merchantDTO){
        Merchant merchant = new Merchant(merchantDTO);
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
