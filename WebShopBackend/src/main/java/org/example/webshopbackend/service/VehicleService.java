package org.example.webshopbackend.service;

import org.example.webshopbackend.domain.Vehicle;

import java.sql.Date;
import java.util.List;

public interface VehicleService {
    public List<Vehicle> getAll();
    public Vehicle getById(Long id);
    public List<Vehicle> getAvailable(Date from, Date to);
}
