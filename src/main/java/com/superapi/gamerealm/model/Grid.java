package com.superapi.gamerealm.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Grid {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The width and height of the grid
    @Column(nullable = false)
    private int width;

    @Column(nullable = false)
    private int height;

    // List of villages on the grid
    @OneToMany(mappedBy = "grid", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Village> villages = new ArrayList<>();

    // List of conquerable spots on the grid
    @OneToMany(mappedBy = "grid", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ConquerableSpot> conquerableSpots = new ArrayList<>();

    // List of barbarian villages on the grid
    @OneToMany(mappedBy = "grid", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BarbarianVillage> barbarianVillages = new ArrayList<>();

    public Grid(int width, int height) {
        this.width = width;
        this.height = height;
        this.barbarianVillages = new ArrayList<>();
        this.conquerableSpots = new ArrayList<>();
    }

    public Grid() {

    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public List<Village> getVillages() {
        return villages;
    }

    public void setVillages(List<Village> villages) {
        this.villages = villages;
    }

    public List<ConquerableSpot> getConquerableSpots() {
        return conquerableSpots;
    }

    public void setConquerableSpots(List<ConquerableSpot> conquerableSpots) {
        this.conquerableSpots = conquerableSpots;
    }

    public List<BarbarianVillage> getBarbarianVillages() {
        return barbarianVillages;
    }

    public void setBarbarianVillages(List<BarbarianVillage> barbarianVillages) {
        this.barbarianVillages = barbarianVillages;
    }
}
