package org.example.bankbackend.component;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@Component
public class HmacUtil {

    public String generateHmac(String algorithm, String data, String secretKey){
        try{
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), algorithm);

            Mac mac = Mac.getInstance(algorithm);
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
