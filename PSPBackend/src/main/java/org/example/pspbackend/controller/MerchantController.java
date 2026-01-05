package org.example.pspbackend.controller;


import org.example.pspbackend.domain.Merchant;
import org.example.pspbackend.dto.LoginDetailsDTO;
import org.example.pspbackend.dto.MerchantDTO;
import org.example.pspbackend.security.TokenUtil;
import org.example.pspbackend.service.MerchantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "merchant", produces = MediaType.APPLICATION_JSON_VALUE)
public class MerchantController {
    @Autowired
    private MerchantService merchantService;
    @Autowired
    private TokenUtil tokenUtil;

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

    @PutMapping()
    public ResponseEntity<MerchantDTO> update(
            @RequestBody MerchantDTO merchantDTO
    ) {
        Merchant merchant = merchantService.update(merchantDTO);
        return ResponseEntity.ok(new MerchantDTO(merchant));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MerchantDTO> findById(@PathVariable Long id) {
        Merchant merchant = merchantService.getById(id);
        if(merchant == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new MerchantDTO(merchant));
    }
}
