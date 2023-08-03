package com.superapi.gamerealm.model;

import com.superapi.gamerealm.component.Coordinates;
import com.superapi.gamerealm.model.buildings.Building;
import com.superapi.gamerealm.model.resources.Resources;
import com.superapi.gamerealm.model.troop.Troop;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Village {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Coordinates coordinates;
    private String name;

    @ManyToOne
    private Grid grid;
    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @Column
    private LocalDateTime lastUpdated;

    @OneToMany(mappedBy = "village", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Resources> resources;
    @OneToMany(mappedBy = "village", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Building> buildings = new ArrayList<>();
    @OneToMany(mappedBy = "village", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Troop> troops = new ArrayList<>();
    private boolean underAttack;

    public Village() {
        System.out.println("VILLAGE NO ARGS CONSTRUCTOR ");

    }


    public Village(Coordinates coordinates) {
        System.out.println("VILLAGE COORDINATE ARGS CONSTRUCTOR " + coordinates.getX() + " " + coordinates.getY());
        this.coordinates = coordinates;
        this.name = "default name";
        this.lastUpdated = LocalDateTime.now();
    }




    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }


    public void setId(long l) {
        this.id = l;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }



    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public void setGrid(Grid grid) {
        this.grid = grid;
    }

    public Grid getGrid() {
        return grid;
    }


    public List<Resources> getResources() {
        return resources;
    }

    public void setResources(List<Resources> resources) {
        this.resources = resources;
    }
    public List<Building> getBuildings() {
        return buildings;
    }

    public void addBuilding(Building building) {
        buildings.add(building);
        building.setVillage(this);
    }

    public List<Troop> getTroops() {
        return troops;
    }

    public void setTroops(List<Troop> troops) {
        this.troops = troops;
    }

    public boolean isUnderAttack() {
        return underAttack;
    }

    public void setUnderAttack(boolean underAttack) {
        this.underAttack = underAttack;
    }

    public void removeBuilding(Building building) {
        buildings.remove(building);
        building.setVillage(null);
    }

    public void setBuildings(List<Building> buildings) {
        this.buildings = buildings;
    }

    public void setIsUnderAttack(boolean b) {
        this.underAttack = b;
    }


    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}