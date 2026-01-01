package org.example.webshopbackend.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.webshopbackend.dto.UserDTO;

@Getter
@Setter
@Entity
@Table(name= "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name="name")
    private String name;

    @Column(name="surname")
    private String surname;

    @Column(name="email", unique=true)
    private String email;

    @Column(name="password")
    private String password;

    public User() {}

    public User(String name, String surname, String email, String password) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
    }

    public User(UserDTO userDTO) {
        this.name = userDTO.getName();
        this.surname = userDTO.getSurname();
        this.email = userDTO.getEmail();
        this.password = userDTO.getPassword();
    }

    public boolean isValid() {
        return name != null && email != null && password != null && password.length() > 7 && !name.isEmpty() && !surname.isEmpty() && !email.isEmpty();
    }
}
