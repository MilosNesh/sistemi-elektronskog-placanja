package org.example.webshopbackend.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.example.webshopbackend.domain.User;
import org.example.webshopbackend.dto.LoginDetailsDTO;
import org.example.webshopbackend.dto.UserDTO;
import org.example.webshopbackend.security.TokenUtil;
import org.example.webshopbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping(value = "user", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private TokenUtil tokenUtil;

    @PostMapping("/register")
    public ResponseEntity<UserDTO> registration(@RequestBody UserDTO userDTO) {
        User existUser = userService.getByEmail(userDTO.getEmail());
        if (existUser != null) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        User user = userService.register(new User(userDTO));
        if (user != null) {
            return ResponseEntity.ok(new UserDTO(user));
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDetailsDTO loginDetailsDTO) {
        User user = userService.getByEmail(loginDetailsDTO.getEmail());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        if (!userService.login(loginDetailsDTO))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password");
        String jwt = tokenUtil.generateToken(user);
        return ResponseEntity.ok(jwt);
    }
}
