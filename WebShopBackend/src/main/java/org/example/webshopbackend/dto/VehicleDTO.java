package org.example.webshopbackend.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.webshopbackend.domain.Vehicle;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VehicleDTO {
    private Long id;
    private String model;
    private String image;
    private double pricePerDay;
    private String description;
    private String currency;

    public VehicleDTO(Vehicle vehicle) {
        this.id = vehicle.getId();
        this.model = vehicle.getModel();
        this.image = vehicle.getImage();
        this.pricePerDay = vehicle.getPricePerDay();
        this.description = vehicle.getDescription();
        this.currency = vehicle.getCurrency();
    }
}
