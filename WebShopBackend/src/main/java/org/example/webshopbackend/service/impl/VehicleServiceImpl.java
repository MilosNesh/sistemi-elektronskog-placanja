package org.example.webshopbackend.service.impl;

import org.example.webshopbackend.domain.Vehicle;
import org.example.webshopbackend.repository.VehicleRepository;
import org.example.webshopbackend.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleServiceImpl implements VehicleService {
    @Autowired
    private VehicleRepository vehicleRepository;

    @Override
    public List<Vehicle> getAll() {
        return vehicleRepository.findAll();
    }

    @Override
    public Vehicle getById(Long id) {
        return vehicleRepository.findById(id).orElse(null);
    }
}
