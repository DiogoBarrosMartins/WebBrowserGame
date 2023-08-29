package com.superapi.gamerealm.model.troop;

import com.superapi.gamerealm.model.Village;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class TroopTrainingQueue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "village_id")
    private Village village;

    @ManyToOne
    @JoinColumn(name = "troop_type_id")
    private TroopType troopType;

    private int quantity;  // Number of troops being trained

    private LocalDateTime trainingStartTime;

    private LocalDateTime trainingEndTime;

    // Getters, setters, constructors, etc.

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

    public LocalDateTime getTrainingStartTime() {
        return trainingStartTime;
    }

    public void setTrainingStartTime(LocalDateTime trainingStartTime) {
        this.trainingStartTime = trainingStartTime;
    }

    public LocalDateTime getTrainingEndTime() {
        return trainingEndTime;
    }

    public void setTrainingEndTime(LocalDateTime trainingEndTime) {
        this.trainingEndTime = trainingEndTime;
    }
}
