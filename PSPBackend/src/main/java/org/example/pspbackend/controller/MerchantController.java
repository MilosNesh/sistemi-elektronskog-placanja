package org.example.pspbackend.controller;


import org.example.pspbackend.domain.Merchant;
import org.example.pspbackend.dto.LoginDetailsDTO;
import org.example.pspbackend.dto.MerchantDTO;
import org.example.pspbackend.dto.MfaVerificationDTO;
import org.example.pspbackend.dto.RegisterMerchantDTO;
import org.example.pspbackend.security.TokenUtil;
import org.example.pspbackend.service.MerchantService;
import org.example.pspbackend.service.impl.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping(value = "merchant", produces = MediaType.APPLICATION_JSON_VALUE)
public class MerchantController {
    @Autowired
    private MerchantService merchantService;
    @Autowired
    private TokenUtil tokenUtil;
    @Autowired
    private AuthService authService;

    @Value("${server.port}")
    private String port;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDetailsDTO loginDetailsDTO) {
        Merchant merchant = merchantService.getByEmail(loginDetailsDTO.getEmail());
        if(merchant == null) {
            logger.warn("event=LOGIN | user={} | result=FAILURE | description=User not found", loginDetailsDTO.getEmail());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Merchant not found");
        }

        String loginResult = authService.processLogin(loginDetailsDTO);

        if(loginResult.equals("INVALID_PASSWORD")) {
            logger.warn("event=LOGIN | user={} | result=FAILURE | description=Invalid password", loginDetailsDTO.getEmail());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password");
        }
        if(loginResult.startsWith("LOCKED")) {
            logger.warn("event=LOGIN | user={} | result=FAILURE | description=Account locked", loginDetailsDTO.getEmail());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Account locked");
        }

        if (merchantService.sendMfaEmail(merchant)) {
            logger.info("event=MFA_SENT | user={} | result=SUCCESS | description=MFA code sent", loginDetailsDTO.getEmail());

            // Vraćamo informaciju da je potreban kod
            return ResponseEntity.ok(Map.of(
                    "status", "MFA_REQUIRED",
                    "email", merchant.getMerchantEmail()
            ));
        } else {
            logger.error("event=MFA_SENT | user={} | result=FAILURE | description=Email service error", loginDetailsDTO.getEmail());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error sending MFA email. Please try again later.");
        }
    }

    @PostMapping("/verify-mfa")
    public ResponseEntity<?> verifyMfa(@RequestBody MfaVerificationDTO mfaDTO) {
        Merchant merchant = merchantService.getByEmail(mfaDTO.getEmail());

        if( merchantService.verifyCode(mfaDTO)){
            logger.info("event=LOGIN | user={} | result=SUCCESS | description=User logged in via MFA", merchant.getMerchantEmail());
            String jwt = tokenUtil.generateToken(merchant);
            return ResponseEntity.ok(jwt);
        }

        logger.warn("event=MFA_VERIFY | user={} | result=FAILURE | description=Invalid code", mfaDTO.getEmail());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid MFA code");
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
            logger.warn("event=REGISTER | user={} | result=FAILURE | description=User already exist with entered email", merchantDTO.getMerchantEmail());
            return ResponseEntity.notFound().build();
        }
        logger.warn("event=REGISTER | user={} | result=SUCCESS | description=User registered", merchantDTO.getMerchantEmail());
        return ResponseEntity.ok(registerMerchantDTO);
    }
}
