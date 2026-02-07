package org.example.pspbackend.service.impl;

import org.example.pspbackend.domain.Merchant;
import org.example.pspbackend.dto.LoginDetailsDTO;

public interface AuthService {
    public String processLogin(LoginDetailsDTO loginDetailsDTO);
    public void updateFailedAttempts(Merchant merchant);
    public void resetFailedAttempts(Merchant merchant);
}
