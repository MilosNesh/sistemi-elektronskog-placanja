package org.example.webshopbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.webshopbackend.domain.AdditionalService;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdditionalServiceDTO {
    private Long id;
    private String name;
    private double price;
    private String currency;

    public AdditionalServiceDTO(AdditionalService additionalService) {
        this.id = additionalService.getId();
        this.name = additionalService.getName();
        this.price = additionalService.getPrice();
        this.currency = additionalService.getCurrency();
    }
}
