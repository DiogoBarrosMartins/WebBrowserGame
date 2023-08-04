package com.superapi.gamerealm.model;

import javax.persistence.*;


@Entity
public class GameServer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private boolean initialized;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }


    // Add constructors, getters, setters, and other methods as needed
}
