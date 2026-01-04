package org.example.webshopbackend.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.webshopbackend.domain.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDTO {
    private UUID id;
    private double totalPrice;
    private String currency;
    private Date dateFrom;
    private Date dateTo;
    private PaymentStatus paymentStatus;
    private UserDTO userDTO;
    private InsuranceDTO insuranceDTO;
    private VehicleDTO vehicleDTO;
    private List<AdditionalServiceDTO> additionalServiceDTOs = new ArrayList<>();

    public ReservationDTO(Reservation reservation) {
        this.id = reservation.getId();
        this.totalPrice = reservation.getTotalPrice();
        this.currency = reservation.getCurrency();
        this.dateFrom = reservation.getDateFrom();
        this.dateTo = reservation.getDateTo();
        this.paymentStatus = reservation.getPaymentStatus();
        if(reservation.getUser() != null)
            this.userDTO = new UserDTO(reservation.getUser());
        else
            this.userDTO = null;
        if(reservation.getInsurance() != null)
            this.insuranceDTO = new InsuranceDTO(reservation.getInsurance());
        else
            this.insuranceDTO = null;
        if(reservation.getVehicle() != null)
            this.vehicleDTO = new VehicleDTO(reservation.getVehicle());
        else
            this.vehicleDTO = null;
        if(reservation.getAdditionalServices() != null)
            for (AdditionalService additionalService : reservation.getAdditionalServices()) {
                this.additionalServiceDTOs.add(new AdditionalServiceDTO(additionalService));
            }
        else
            this.additionalServiceDTOs = null;
    }

}
