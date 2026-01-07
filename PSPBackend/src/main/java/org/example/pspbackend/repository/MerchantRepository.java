package org.example.pspbackend.repository;

import org.example.pspbackend.domain.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MerchantRepository extends JpaRepository<Merchant, Long> {
    Merchant findByMerchantEmail(String merchantEmail);
}
