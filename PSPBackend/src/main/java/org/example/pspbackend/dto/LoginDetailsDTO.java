package org.example.pspbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginDetailsDTO {
    private String email;
    private String password;

    public String getEmail() { return email; }

    public String getPassword() { return password; }
}
