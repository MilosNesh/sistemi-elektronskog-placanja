package org.example.pspbackend.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "merchant_id", nullable = false)
    private Merchant merchant;

    @Column(name = "amount")
    private double amount;

    @Column(name = "currency")
    private String currency;

    @Column(name = "merchant_timestamp")
    private LocalDateTime merchantTimestamp;

    @Column(name = "merchant_order_id")
    private String merchantOrderId;

    @Column(name = "psp_timestamp")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date pspTimestamp;

    @Column(name = "payment_id")
    private Long paymentId;

    @Column(name = "acquirer_timestamp")
    private Date acquirerTimestamp;

    @Column(name = "global_transaction_id")
    private String globalTransactionId;

    @Column(name = "stan")
    private String stan;

    @Column(name = "status")
    private String status;

    public Transaction() {}

    public Transaction(Merchant merchant, double amount, String currency, LocalDateTime merchantTimestamp, String merchantOrderId, Date pspTimestamp) {
        this.merchant = merchant;
        this.amount = amount;
        this.currency = currency;
        this.merchantTimestamp = merchantTimestamp;
        this.pspTimestamp = pspTimestamp;
        this.merchantOrderId = merchantOrderId;
    }

    public String getId() {
        return id;
    }

    public Merchant getMerchant() {
        return merchant;
    }

    public double getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public LocalDateTime getMerchantTimestamp() {
        return merchantTimestamp;
    }

    public String getMerchantOrderId() {
        return merchantOrderId;
    }

    public Date getPspTimestamp() {
        return pspTimestamp;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public Date getAcquirerTimestamp() {
        return acquirerTimestamp;
    }

    public String getGlobalTransactionId() {
        return globalTransactionId;
    }

    public String getStan() {
        return stan;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setMerchantTimestamp(LocalDateTime merchantTimestamp) {
        this.merchantTimestamp = merchantTimestamp;
    }

    public void setMerchantOrderId(String merchantOrderId) {
        this.merchantOrderId = merchantOrderId;
    }

    public void setPspTimestamp(Date pspTimestamp) {
        this.pspTimestamp = pspTimestamp;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public void setAcquirerTimestamp(Date acquirerTimestamp) {
        this.acquirerTimestamp = acquirerTimestamp;
    }

    public void setGlobalTransactionId(String globalTransactionId) {
        this.globalTransactionId = globalTransactionId;
    }

    public void setStan(String stan) {
        this.stan = stan;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}
