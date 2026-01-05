package org.example.pspbackend.repository;

import org.example.pspbackend.domain.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MerchantRepository extends JpaRepository<Merchant, Long> {
    Merchant findByMerchantEmail(String merchantEmail);
}
