package org.example.pspbackend.service.impl;

import org.example.pspbackend.domain.Merchant;
import org.example.pspbackend.domain.MerchantPaymentMethod;
import org.example.pspbackend.domain.PaymentMethod;
import org.example.pspbackend.domain.Role;
import org.example.pspbackend.dto.*;
import org.example.pspbackend.repository.MerchantRepository;
import org.example.pspbackend.repository.PaymentMethodRepository;
import org.example.pspbackend.security.PasswordHasher;
import org.example.pspbackend.service.MerchantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class MerchantServiceImpl implements MerchantService {
    @Autowired
    private MerchantRepository merchantRepository;
    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    @Autowired
    private EmailServiceImpl emailService;

    public Merchant getById(Long id){
        return merchantRepository.findById(id).orElse(null);
    }

    public Merchant update(MerchantDTO merchantDTO) {
        System.out.println("--- POCETAK UPDATE-A ZA MERCHANT ID: " + merchantDTO.getMerchantId() + " ---");

        Merchant merchant = merchantRepository.findById(merchantDTO.getMerchantId())
                .orElseThrow(() -> new RuntimeException("Merchant not found"));

        merchant.setSuccessUrl(merchantDTO.getSuccessUrl());
        merchant.setFailedUrl(merchantDTO.getFailedUrl());
        merchant.setErrorUrl(merchantDTO.getErrorUrl());

        // Loguj trenutne metode u bazi
        System.out.println("Trenutni payment metodi u bazi: " +
                merchant.getMerchantPaymentMethods().stream()
                        .map(m -> "ID: " + m.getMerchantPaymentMethodId() + " (Type: " + m.getPaymentMethod().getType() + ")")
                        .collect(Collectors.joining(", ")));

        // Mapiranje postojecih
        Map<Long, MerchantPaymentMethod> existing = merchant.getMerchantPaymentMethods()
                .stream()
                .collect(Collectors.toMap(
                        MerchantPaymentMethod::getMerchantPaymentMethodId,
                        Function.identity()
                ));

        List<MerchantPaymentMethod> updated = new ArrayList<>();

        for (MerchantPaymentMethodDTO pmDto : merchantDTO.getMerchantPaymentMethods()) {
            System.out.println("Obrada DTO-a: MerchantPaymentMethodId=" + pmDto.getMerchantPaymentMethodId() +
                    ", PaymentMethodId=" + pmDto.getPaymentMethodId());

            MerchantPaymentMethod mpm;

            if (pmDto.getMerchantPaymentMethodId() != null && existing.containsKey(pmDto.getMerchantPaymentMethodId())) {
                System.out.println("  >> PRONADJEN POSTOJECI: Azuriram ID " + pmDto.getMerchantPaymentMethodId());
                mpm = existing.get(pmDto.getMerchantPaymentMethodId());
            } else {
                System.out.println("  >> KREIRAM NOVI: Nije pronadjen ID u mapi.");
                mpm = new MerchantPaymentMethod();
                mpm.setMerchant(merchant);
            }

            mpm.setEnabled(pmDto.getEnabled());
            PaymentMethod paymentMethod = paymentMethodRepository
                    .findById(pmDto.getPaymentMethodId())
                    .orElseThrow(() -> new RuntimeException("PaymentMethod not found"));
            mpm.setPaymentMethod(paymentMethod);

            updated.add(mpm);
        }

        try {
            System.out.println("Pokusavam .clear() na listi velicine: " + merchant.getMerchantPaymentMethods().size());
            merchant.getMerchantPaymentMethods().clear();

            System.out.println("Pokusavam .addAll() sa novom listom velicine: " + updated.size());
            merchant.getMerchantPaymentMethods().addAll(updated);
        } catch (Exception e) {
            System.err.println("GRESKA KOD RADA SA LISTOM: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }

        System.out.println("--- KRAJ UPDATE-A: Snimam u bazu ---");
        return merchantRepository.save(merchant);
    }

    public Merchant getByEmail(String email){
        return merchantRepository.findByMerchantEmail(email);
    }

    public boolean login(LoginDetailsDTO loginDetailsDTO){
        Merchant merchant = getByEmail(loginDetailsDTO.getEmail());
        return PasswordHasher.verifyPassword(loginDetailsDTO.getPassword(), merchant.getMerchantPassword());
    }

    @Override
    public RegisterMerchantDTO save(RegisterMerchantDTO merchantDTO) {
        Merchant merchant = new Merchant();

        merchant.setMerchantEmail(merchantDTO.getMerchantEmail());
        merchant.setMerchantPassword(PasswordHasher.hashPassword((merchantDTO.getPassword())));
        merchant.setSellerUrl(merchantDTO.getSellerUrl());

        merchant.setRole(Role.ROLE_MERCHANT);

        PaymentMethod creditCardPaymentMethod = paymentMethodRepository.findById(1L).orElse(null);
        PaymentMethod qrCodePaymentMethod = paymentMethodRepository.findById(2L).orElse(null);
        MerchantPaymentMethod merchantPaymentMethodCard = new MerchantPaymentMethod(merchant, creditCardPaymentMethod);
        MerchantPaymentMethod merchantPaymentMethodQr = new MerchantPaymentMethod(merchant, qrCodePaymentMethod);
        ArrayList<MerchantPaymentMethod> paymentMethods = new ArrayList<>();
        paymentMethods.add(merchantPaymentMethodCard);
        paymentMethods.add(merchantPaymentMethodQr);
        merchant.setMerchantPaymentMethods(paymentMethods);

        merchantRepository.save(merchant);

        return new RegisterMerchantDTO(merchant);
    }

    @Override
    public List<MerchantDTO> getAll() {
        List<Merchant> merchants = merchantRepository.findAll();

        return merchants.stream()
                .filter(merchant -> merchant.getRole() == Role.ROLE_MERCHANT)
                .map(merchant -> new MerchantDTO(merchant))
                .toList();
    }

    public boolean sendMfaEmail(Merchant merchant){
        String code = String.format("%06d", new Random().nextInt(1000000));

        merchant.setMfaCode(code);
        merchantRepository.save(merchant);

        try {
            merchantRepository.save(merchant);
            emailService.sendMfaCode(merchant.getMerchantEmail(), code);
            System.out.println("Mejl uspesno poslat: " + code + "  " + merchant.getMerchantEmail());
            return true;
        } catch (Exception e) {
            System.out.println("Mejl nije poslat");
            return false;
        }
    }

    public boolean verifyCode(MfaVerificationDTO mfaDTO){

        Merchant merchant = merchantRepository.findByMerchantEmail(mfaDTO.getEmail());
        if (merchant != null && mfaDTO.getCode().equals(merchant.getMfaCode())) {
            merchant.setMfaCode(null);
            merchantRepository.save(merchant);
            System.out.println("Code successfuly verified");
            return true;
        }else{
            System.out.println("Code not verified, code in database: " + merchant.getMfaCode() + ", received code: " + mfaDTO.getCode());
            return false;
        }
    }

}
