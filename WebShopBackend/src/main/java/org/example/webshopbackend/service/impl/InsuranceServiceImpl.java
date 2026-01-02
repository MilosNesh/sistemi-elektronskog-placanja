package org.example.webshopbackend.service.impl;

import org.example.webshopbackend.domain.Insurance;
import org.example.webshopbackend.repository.InsuranceRepository;
import org.example.webshopbackend.service.InsuranceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InsuranceServiceImpl implements InsuranceService {
    @Autowired
    private InsuranceRepository insuranceRepository;

    @Override
    public List<Insurance> getAll() {
        return insuranceRepository.findAll();
    }

    @Override
    public Insurance getById(Long id) {
        return insuranceRepository.findById(id).orElse(null);
    }
}
