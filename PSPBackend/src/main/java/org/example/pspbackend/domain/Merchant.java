package org.example.pspbackend.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "merchants")
@Data
public class Merchant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "merchant_id")
    private Long merchantId;

    @OneToMany(
            mappedBy = "merchant",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<PaymentMethod> paymentMethods = new ArrayList<>();

    @Column(name = "merchant_username",
            nullable = false,
            unique = true,
            length = 50)
    private String merchantUsername;

    @Column(nullable = false, name = "merchant_password")
    private String merchantPassword;

    @Column(name = "seller_url")
    private String sellerUrl;

    @Column(name = "port")
    private Integer port;

    @Column(name = "success_url")
    private String successUrl;

    @Column(name = "failed_url")
    private String failedUrl;

    @Column(name = "error_url")
    private String errorUrl;

}
