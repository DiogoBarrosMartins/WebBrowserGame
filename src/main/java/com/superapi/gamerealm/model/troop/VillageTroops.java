package com.superapi.gamerealm.model.troop;

import com.superapi.gamerealm.model.Village;
import jakarta.persistence.*;

@Entity
public class VillageTroops {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "village_id")
    private Village village;

    @Enumerated(EnumType.STRING)
    private TroopType troopType;

    private int quantity;

    public VillageTroops(Village village, TroopType troopType, int quantity) {
        this.village = village;
        this.troopType = troopType;
        this.quantity = quantity;
    }

    public VillageTroops() {

    }

    // Add Troops
    public void addTroops(int quantity) {
        this.quantity += quantity;
    }

    // Remove Troops
    public void removeTroops(int quantity) {
        this.quantity -= quantity;
        if (this.quantity < 0) {
            this.quantity = 0;
        }
    }

    // ... getters, setters, etc.

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Village getVillage() {
        return village;
    }

    public void setVillage(Village village) {
        this.village = village;
    }

    public TroopType getTroopType() {
        return troopType;
    }

    public void setTroopType(TroopType troopType) {
        this.troopType = troopType;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
