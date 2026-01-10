package org.example.pspbackend.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
    private Date merchantTimestamp;

    @Column(name = "merchant_order_id")
    private String merchantOrderId;

    @Column(name = "psp_timestamp")
    private Date pspTimestamp;

    public Transaction(Merchant merchant, double amount, String currency, Date merchantTimestamp, String merchantOrderId, Date pspTimestamp) {
        this.merchant = merchant;
        this.amount = amount;
        this.currency = currency;
        this.merchantTimestamp = merchantTimestamp;
        this.pspTimestamp = pspTimestamp;
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

    public Date getMerchantTimestamp() {
        return merchantTimestamp;
    }

    public String getMerchantOrderId() {
        return merchantOrderId;
    }

    public Date getPspTimestamp() {
        return pspTimestamp;
    }
}
