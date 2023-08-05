package com.superapi.gamerealm.model;

import jakarta.persistence.*;


@Entity
public class GameServer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private boolean initialized;
    @OneToOne
    @JoinColumn(name = "grid_id")
    // Add other fields, constructors, and getters/setters (if needed)

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
