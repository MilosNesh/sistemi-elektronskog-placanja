package org.example.webshopbackend.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.webshopbackend.dto.AdditionalServiceDTO;

import java.util.*;

import java.util.ArrayList;

@Getter
@Setter
@Entity
@Table(name = "additional_service")
public class AdditionalService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private double price;

    @Column(name = "currency")
    private String currency;

    @ManyToMany(mappedBy = "additionalServices")
    List<Reservation> reservations = new ArrayList<>();

    public AdditionalService() {}

    public AdditionalService(String name, double price, String currency) {
        this.name = name;
        this.price = price;
        this.currency = currency;
    }

    public AdditionalService(AdditionalServiceDTO dto) {
        this.id = dto.getId();
        this.name = dto.getName();
        this.price = dto.getPrice();
        this.currency = dto.getCurrency();
    }
}
