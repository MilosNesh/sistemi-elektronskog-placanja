package org.example.pspbackend.component;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

@Component
public class HmacUtil {

    private static final String HMAC_ALGORITHM = "HmacSHA256";

    @Value("${psp.shared-secret}")
    private String secretKey;

    public String generateHmac(String data){
        try{
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), HMAC_ALGORITHM);

            Mac mac = Mac.getInstance(HMAC_ALGORITHM);
            mac.init(secretKeySpec);

            byte[] rawHmac = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(rawHmac);

        } catch(Exception e){
            throw new RuntimeException("Error while calculating HMAC", e);
        }
    }

    private String bytesToHex(byte[] bytes){
        StringBuilder hex = new StringBuilder(bytes.length * 2);
        for(byte b : bytes){
            hex.append(String.format("%02x", b));
        }
        return hex.toString();
    }
}
