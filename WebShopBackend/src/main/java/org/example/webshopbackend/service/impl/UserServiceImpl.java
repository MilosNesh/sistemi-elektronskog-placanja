package org.example.webshopbackend.service.impl;

import org.example.webshopbackend.domain.User;
import org.example.webshopbackend.dto.LoginDetailsDTO;
import org.example.webshopbackend.repository.UserRepository;
import org.example.webshopbackend.security.PasswordHasher;
import org.example.webshopbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public User register(User user) {
        if(!user.isValid())
            return null;
        user.setPassword(PasswordHasher.hashPassword(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User getByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public boolean login(LoginDetailsDTO loginDetailsDTO) {
        User user = getByEmail(loginDetailsDTO.getEmail());
        return PasswordHasher.verifyPassword(loginDetailsDTO.getPassword(), user.getPassword());
    }
}
