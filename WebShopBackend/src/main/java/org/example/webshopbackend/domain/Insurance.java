package org.example.webshopbackend.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.webshopbackend.dto.InsuranceDTO;

@Getter
@Setter
@Entity
@Table(name = "insurances")
public class Insurance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "price_per_day")
    private double pricePerDay;

    @Column(name = "currency")
    private String currency;

    public Insurance() {}

    public Insurance(String name, double pricePerDay, String currency) {
        this.name = name;
        this.pricePerDay = pricePerDay;
        this.currency = currency;
    }

    public Insurance(InsuranceDTO insuranceDTO) {
        this.id = insuranceDTO.getId();
        this.name = insuranceDTO.getName();
        this.pricePerDay = insuranceDTO.getPricePerDay();
        this.currency = insuranceDTO.getCurrency();
    }
}
