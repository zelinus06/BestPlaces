package com.bestplaces.Entity;

import com.bestplaces.Enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

@Entity
@Table(name =  "user", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"email"}),
        @UniqueConstraint(columnNames = {"username"})
})
public class User {
    @Getter
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Column(name = "username")
    private String username;

    @Getter
    private String email;

    @Getter
    private String password;

    @Getter
    private String phoneNumber;

    @Getter
    @Enumerated(EnumType.STRING)
    private Role role;

    @Getter
    @Setter
    private String avatar;

    private boolean authenticated = false;

    public User() {
    }

    public User(String username, String email, String password, Role role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }
}