package org.example.pspbackend.dto;

public class MfaVerificationDTO {
    private String email;
    private String code;

    public String getEmail() {
        return email;
    }

    public String getCode() {
        return code;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setCode(String code) {
        this.code = code;
    }
}
