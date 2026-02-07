package org.example.bankbackend.service;

import org.example.bankbackend.component.HmacUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.json.JsonParseException;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

@Service
public class PspAuthService {
    private static final String HMAC_ALGORITHM = "HmacSHA256";

    @Value("${psp.shared-secret}")/// TAJNI KLJUC
    private String sharedSecret;

    private final HmacUtil hmacUtil;
    private final ObjectMapper objectMapper;

    public PspAuthService(HmacUtil hmacUtil, ObjectMapper objectMapper) {
        this.hmacUtil = hmacUtil;
        this.objectMapper = objectMapper;
    }

    public void validateRequest(Object requestBody, String receivedSignature){
        try{
            String dataToSign = objectMapper.writeValueAsString(requestBody);

            String expectedSignature = hmacUtil.generateHmac(HMAC_ALGORITHM, dataToSign, sharedSecret);
            System.out.println("EXPECTED:" + expectedSignature);
            System.out.println("RECEIVED:" + receivedSignature);
            if(!expectedSignature.equals(receivedSignature)){
                throw new SecurityException("Invalid HMAC signature");
            }
        } catch(JsonParseException e){
            throw new RuntimeException("Unable to serialize request body", e);
        }
    }
}
