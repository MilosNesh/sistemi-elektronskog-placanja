package org.example.webshopbackend.service;

import org.example.webshopbackend.domain.Insurance;
import java.util.List;

public interface InsuranceService {
    public List<Insurance> getAll();
    public Insurance getById(Long id);
}
