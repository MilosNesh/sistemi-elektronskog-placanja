package org.example.pspbackend.controller;


import org.example.pspbackend.domain.Merchant;
import org.example.pspbackend.dto.MerchantDTO;
import org.example.pspbackend.service.MerchantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "merchant", produces = MediaType.APPLICATION_JSON_VALUE)
public class MerchantController {
    @Autowired
    private MerchantService merchantService;

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
