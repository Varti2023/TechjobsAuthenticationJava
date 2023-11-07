package org.launchcode.techjobsauth.models;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Entity
public class User extends AbstractEntity{

    @NotNull
    private String userName;
    @NotNull
    private String pwHash;
    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public User(String userName, String password) {
        this.userName = userName;
        this.pwHash = encoder.encode(password);
    }

    public User() {
    }

    public String getUserName() {
        return userName;
    }

    public boolean isMatchingPassword(String password) {
        return encoder.matches(password, pwHash);
    }
}
