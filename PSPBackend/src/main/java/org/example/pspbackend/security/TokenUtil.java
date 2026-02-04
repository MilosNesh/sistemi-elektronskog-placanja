package org.example.pspbackend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import org.example.pspbackend.domain.Merchant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TokenUtil {
    @Value("web-shop-app")
    private String APP_NAME;

    @Value("somesecret")
    public String SECRET;

    @Value("18000000")
    private int EXPIRES_IN;

    @Value("Authorization")
    private String AUTH_HEADER;

    private SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS512;

    public String generateToken(Merchant merchant) {
        Claims claims = Jwts.claims().setSubject(merchant.getMerchantEmail());
        claims.put("role", merchant.getRole().name());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuer(APP_NAME)
                .setSubject(merchant.getMerchantEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + EXPIRES_IN))
                .signWith(SIGNATURE_ALGORITHM, SECRET).compact();
    }

    public String getRoleFromToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        return claims.get("role", String.class);
    }

    public String getToken(HttpServletRequest request) {
        String authHeader = request.getHeader(AUTH_HEADER);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }

        return null;
    }

    public Boolean validateToken(String token, Merchant merchant) {
        final String email = getEmailFromToken(token);
        final Date expiration = getExpiration(token);
        System.out.println("email tokennn: " + email + " expiration: " + expiration);

        return (email != null
                && email.equals(merchant.getMerchantEmail())
                && expiration != null && expiration.after(new Date()));
    }

    public String getEmailFromToken(String token) {
        String email;

        try {
            final Claims claims = this.getAllClaimsFromToken(token);
            email = claims.getSubject();
        } catch (ExpiredJwtException ex) {
            throw ex;
        } catch (Exception e) {
            email = null;
        }

        return email;
    }

    private Claims getAllClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException ex) {
            throw ex;
        } catch (Exception e) {
            claims = null;
        }

        return claims;
    }

    public Date getExpiration(String token) {
        Date expiration;
        try {
            final Claims claims = this.getAllClaimsFromToken(token);
            expiration = claims.getExpiration();
        } catch (ExpiredJwtException ex) {
            throw ex;
        } catch (Exception e) {
            expiration = null;
        }
        return expiration;
    }
}
