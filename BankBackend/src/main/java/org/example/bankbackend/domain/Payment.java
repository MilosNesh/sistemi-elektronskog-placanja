package org.example.bankbackend.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.bankbackend.domain.enums.PaymentStatus;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Merchant merchant;

    @ManyToOne
    //@JoinColumn(name = "customer_id")
    private Customer customer;

    private Double amount;
    private String currency;

    @Column(unique = true)
    private String stan; // samo za karticu

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    @Column(nullable = false)
    private Integer attemptCount;

    private String globalTransactionId;

    private LocalDateTime acquirerTimestamp;
    private String pspTimestamp;

    public Long getId() {
        return id;
    }

    public Merchant getMerchant() {
        return merchant;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Double getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public String getStan() {
        return stan;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public String getGlobalTransactionId() {
        return globalTransactionId;
    }

    public Integer getAttemptCount() {
        return attemptCount;
    }

    public LocalDateTime getAcquirerTimestamp() {
        return acquirerTimestamp;
    }

    public String getPspTimestamp() {
        return pspTimestamp;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setStan(String stan) {
        this.stan = stan;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public void setAttemptCount(Integer attemptCount) {
        this.attemptCount = attemptCount;
    }

    public void setGlobalTransactionId(String globalTransactionId) {
        this.globalTransactionId = globalTransactionId;
    }

    public void setAcquirerTimestamp(LocalDateTime acquirerTimestamp) {
        this.acquirerTimestamp = acquirerTimestamp;
    }

    public void setPspTimestamp(String pspTimestamp) {
        this.pspTimestamp = pspTimestamp;
    }
}
