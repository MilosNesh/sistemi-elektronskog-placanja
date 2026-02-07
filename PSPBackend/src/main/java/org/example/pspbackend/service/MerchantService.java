package org.example.pspbackend.service;

import org.example.pspbackend.domain.Merchant;
import org.example.pspbackend.dto.LoginDetailsDTO;
import org.example.pspbackend.dto.MerchantDTO;
import org.example.pspbackend.dto.MfaVerificationDTO;
import org.example.pspbackend.dto.RegisterMerchantDTO;

import java.util.List;

public interface MerchantService {
    public Merchant getById(Long id);
    public Merchant update(MerchantDTO merchantDTO);
    public Merchant getByEmail(String email);
    public boolean login(LoginDetailsDTO loginDetailsDTO);
    public List<MerchantDTO> getAll();
    public RegisterMerchantDTO save(RegisterMerchantDTO merchantDTO);
    public boolean sendMfaEmail(Merchant merchant);
    public boolean verifyCode(MfaVerificationDTO verificationDTO);
}
