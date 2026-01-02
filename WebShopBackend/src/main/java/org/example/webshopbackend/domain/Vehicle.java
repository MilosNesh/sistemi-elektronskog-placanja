package org.example.webshopbackend.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.webshopbackend.dto.VehicleDTO;

@Getter
@Setter
@Entity
@Table(name = "vehicles")
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "model")
    private String model;

    @Column(name = "image")
    private String image;

    @Column(name = "price_per_day")
    private double pricePerDay;

    @Column(name = "description")
    private String description;

    @Column(name = "currency")
    private String currency;

    public Vehicle() {}

    public Vehicle(String model, String image, double pricePerDay, String description, String currency) {
        this.model = model;
        this.image = image;
        this.pricePerDay = pricePerDay;
        this.description = description;
        this.currency = currency;
    }

    public Vehicle(VehicleDTO vehicleDTO) {
        this.id = vehicleDTO.getId();
        this.model = vehicleDTO.getModel();
        this.image = vehicleDTO.getImage();
        this.pricePerDay = vehicleDTO.getPricePerDay();
        this.description = vehicleDTO.getDescription();
        this.currency = vehicleDTO.getCurrency();
    }
}
