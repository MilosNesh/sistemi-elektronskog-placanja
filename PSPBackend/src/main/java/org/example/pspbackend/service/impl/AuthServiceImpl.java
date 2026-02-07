package org.example.pspbackend.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.example.pspbackend.domain.Merchant;
import org.example.pspbackend.dto.LoginDetailsDTO;
import org.example.pspbackend.repository.MerchantRepository;
import org.example.pspbackend.security.PasswordHasher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private MerchantRepository merchantRepository;

    private static final int MAX_FAILED_ATTEMPTS = 5;
    private static final int LOCK_TIME_DURATION_MINUTES = 30;
    public String processLogin(LoginDetailsDTO loginDetailsDTO) {
        Merchant merchant = merchantRepository.findByMerchantEmail(loginDetailsDTO.getEmail());

        if (merchant.getLockUntil() != null && merchant.getLockUntil().isAfter(LocalDateTime.now())) {
            long minutesLeft = Duration.between(LocalDateTime.now(), merchant.getLockUntil()).toMinutes();
            return "LOCKED:" + (minutesLeft > 0 ? minutesLeft : 1);
        }

        if (!PasswordHasher.verifyPassword(loginDetailsDTO.getPassword(), merchant.getMerchantPassword())) {
            updateFailedAttempts(merchant);
            return "INVALID_PASSWORD";
        }

        resetFailedAttempts(merchant);
        return "SUCCESS";
    }

    public void updateFailedAttempts(Merchant merchant) {
        int newAttempts = merchant.getFailedAttempts() + 1;
        merchant.setFailedAttempts(newAttempts);

        if (newAttempts >= 5) {
            merchant.setLockUntil(LocalDateTime.now().plusMinutes(30));
        }
        merchantRepository.save(merchant);
    }

    public void resetFailedAttempts(Merchant merchant) {
        merchant.setFailedAttempts(0);
        merchant.setLockUntil(null);
        merchantRepository.save(merchant);
    }
}
