package org.example.bankbackend.repository;

import org.example.bankbackend.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByAccountNumber(String accountNumber);
    Optional<Customer> findByCardLast4(String last4);
    Optional<Customer> findByCardLast4AndFullName(String last4, String fullName);
}
