package org.example.webshopbackend.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.webshopbackend.dto.AdditionalServiceDTO;
import org.example.webshopbackend.dto.ReservationDTO;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "reservation")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "total_price")
    private double totalPrice;

    @Column(name = "currency")
    private String currency;

    @Column(name = "date_from")
    private Date dateFrom;

    @Column(name = "date_to")
    private Date dateTo;

    @Column(name = "payment_status")
    private PaymentStatus paymentStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Insurance insurance;

    @ManyToOne(fetch = FetchType.LAZY)
    private Vehicle vehicle;

    @ManyToMany
    @JoinTable(
            name = "reservation_additional_service",
            joinColumns = @JoinColumn(name = "reservation_id"),
            inverseJoinColumns = @JoinColumn(name = "additional_service_id"))
    private List<AdditionalService> additionalServices = new ArrayList<>();

    public Reservation() {}

    public Reservation(double totalPrice, String currency, Date dateFrom, Date dateTo,
                       PaymentStatus paymentStatus, User user, Insurance insurance, Vehicle vehicle,
                       List<AdditionalService> additionalServices) {
        this.totalPrice = totalPrice;
        this.currency = currency;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.paymentStatus = paymentStatus;
        this.user = user;
        this.insurance = insurance;
        this.vehicle = vehicle;
        if (additionalServices != null) {
            this.additionalServices = additionalServices;
        } else {
            this.additionalServices = new ArrayList<>();
        }
    }

    public Reservation(ReservationDTO reservationDTO) {
        this.id = reservationDTO.getId();
        this.totalPrice = reservationDTO.getTotalPrice();
        this.currency = reservationDTO.getCurrency();
        this.dateFrom = reservationDTO.getDateFrom();
        this.dateTo = reservationDTO.getDateTo();
        this.paymentStatus = reservationDTO.getPaymentStatus();
        if(reservationDTO.getUserDTO() != null)
            this.user = new User(reservationDTO.getUserDTO());
        else
            this.user = null;
        if(reservationDTO.getInsuranceDTO() != null)
            this.insurance = new Insurance(reservationDTO.getInsuranceDTO());
        else
            this.insurance = null;
        if(reservationDTO.getVehicleDTO() != null)
            this.vehicle = new Vehicle(reservationDTO.getVehicleDTO());
        else
            this.vehicle = null;
        if(reservationDTO.getAdditionalServiceDTOs() != null)
            for (AdditionalServiceDTO additionalServiceDTO : reservationDTO.getAdditionalServiceDTOs()) {
                this.additionalServices.add(new AdditionalService(additionalServiceDTO));
            }
        else
            this.additionalServices = new ArrayList<>();
    }
}

