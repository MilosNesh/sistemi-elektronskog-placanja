package org.example.webshopbackend.service.impl;

import org.example.webshopbackend.domain.AdditionalService;
import org.example.webshopbackend.repository.AdditionalServiceRepository;
import org.example.webshopbackend.service.AdditionalServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdditionalServiceServiceImpl implements AdditionalServiceService {
    @Autowired
    private AdditionalServiceRepository additionalServiceRepository;

    @Override
    public List<AdditionalService> getAll() {
        return additionalServiceRepository.findAll();
    }
}
