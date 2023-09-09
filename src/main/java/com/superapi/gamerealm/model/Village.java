package com.superapi.gamerealm.model;

import com.superapi.gamerealm.model.buildings.Building;
import com.superapi.gamerealm.model.buildings.Construction;
import com.superapi.gamerealm.model.resources.Resources;
import com.superapi.gamerealm.model.troop.TroopTrainingQueue;
import com.superapi.gamerealm.model.troop.VillageTroops;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Village {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private int x;
    @Column
    private int y;

    private String name;


    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @Column
    private LocalDateTime lastUpdated;


    //THIS HAS TO BE CHANGED TO A SINGLE OBJECT INSTEAD OF A LIST
    @OneToMany(mappedBy = "village", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Resources>resources;
    @OneToMany(mappedBy = "village", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Building> buildings = new ArrayList<>();


    @OneToMany(mappedBy = "village", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Construction> constructions = new ArrayList<>();

    // Bidirectional mapping to VillageTroops
    @OneToMany(mappedBy = "village", cascade = CascadeType.ALL)
    private List<VillageTroops> troops;

    @OneToMany(mappedBy = "village", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TroopTrainingQueue> troopQueue;
    private boolean underAttack;

    public Village() {
    }


    public Village(int x, int y) {
        this.x = x;
        this.y = y;
        this.name = "default name";
        this.lastUpdated = LocalDateTime.now();
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
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


    public List<Construction> getConstructions() {
        return constructions;
    }

    public void setConstructions(List<Construction> constructions) {
        this.constructions = constructions;
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

    public List<VillageTroops> getTroops() {
        return troops;
    }

    public void setTroops(List<VillageTroops> troops) {
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

    public List<TroopTrainingQueue> getTroopQueue() {
        return troopQueue;
    }

    public void setTroopQueue(List<TroopTrainingQueue> troopQueue) {
        this.troopQueue = troopQueue;
    }
}