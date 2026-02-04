package org.example.pspbackend.controller;


import org.example.pspbackend.domain.Merchant;
import org.example.pspbackend.dto.LoginDetailsDTO;
import org.example.pspbackend.dto.MerchantDTO;
import org.example.pspbackend.dto.RegisterMerchantDTO;
import org.example.pspbackend.security.TokenUtil;
import org.example.pspbackend.service.MerchantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "merchant", produces = MediaType.APPLICATION_JSON_VALUE)
public class MerchantController {
    @Autowired
    private MerchantService merchantService;
    @Autowired
    private TokenUtil tokenUtil;

    @Value("${server.port}")
    private String port;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDetailsDTO loginDetailsDTO) {
        Merchant merchant = merchantService.getByEmail(loginDetailsDTO.getEmail());
        if(merchant == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Merchant not found");
        }
        if(!merchantService.login(loginDetailsDTO))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password");

        String jwt = tokenUtil.generateToken(merchant);
        return ResponseEntity.ok(jwt);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MerchantDTO> update(
            @RequestBody MerchantDTO merchantDTO
    ) {
        Merchant merchant = merchantService.update(merchantDTO);
        return ResponseEntity.ok(new MerchantDTO(merchant));
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<MerchantDTO> findById(@PathVariable Long id) {
        Merchant merchant = merchantService.getById(id);
        String message = "Zahtev obrađen na PSP instanci koja radi na portu: " + port;
        System.out.println(message);
        if(merchant == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new MerchantDTO(merchant));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<MerchantDTO> findByEmail(@PathVariable String email) {
        Merchant merchant = merchantService.getByEmail(email);
        if(merchant == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new MerchantDTO(merchant));
    }

    @GetMapping("/all")
    public ResponseEntity<List<MerchantDTO>> findAll() {
        List<MerchantDTO> merchantDTOs = merchantService.getAll();
        if(merchantDTOs == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(merchantDTOs);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RegisterMerchantDTO> create(@RequestBody RegisterMerchantDTO merchantDTO) {
        RegisterMerchantDTO registerMerchantDTO = merchantService.save(merchantDTO);
        if(registerMerchantDTO == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(registerMerchantDTO);
    }
}
