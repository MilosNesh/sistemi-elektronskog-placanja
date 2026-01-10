package org.example.webshopbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.webshopbackend.domain.Insurance;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InsuranceDTO {
    private Long id;
    private String name;
    private double pricePerDay;
    private String currency;

    public InsuranceDTO(Insurance insurance) {
        this.id = insurance.getId();
        this.name = insurance.getName();
        this.pricePerDay = insurance.getPricePerDay();
        this.currency = insurance.getCurrency();
    }
}
