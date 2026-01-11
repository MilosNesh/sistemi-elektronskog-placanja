package org.example.bankbackend.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;
    private String email;

    private Double balance;

    private String accountNumber;

    @Column(length = 4, nullable = false)
    private String cardLast4;

    public Double getBalance() {
        return balance;
    }
    public void setBalance(Double balance){
        this.balance = balance;
    }
}
