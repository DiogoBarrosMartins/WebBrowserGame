package com.superapi.gamerealm.model;

import jakarta.persistence.*;

@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL)
    private Village village;


    private String username;
    private String password;
    private String email;
    private String tribe;

    public Account() {
    }

    public Account(String username, String password, String email, String tribe) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.tribe = tribe;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setVillage(Village newVillage) {
   this.village = newVillage;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTribe() {
        return tribe;
    }

    public void setTribe(String tribe) {
        this.tribe = tribe;
    }

    public Village getVillage() {
        return village;
    }
// getters and setters
}
