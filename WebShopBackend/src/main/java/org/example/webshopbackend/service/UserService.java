package org.example.webshopbackend.service;

import org.example.webshopbackend.domain.User;
import org.example.webshopbackend.dto.LoginDetailsDTO;

public interface UserService {
    public User register(User user);
    public User getByEmail(String email);
    public boolean login(LoginDetailsDTO loginDetailsDTO);
}
