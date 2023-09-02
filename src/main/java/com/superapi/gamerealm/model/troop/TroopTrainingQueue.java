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

    @Enumerated(EnumType.STRING) // or EnumType.ORDINAL  to map to int
    @Column(name = "troop_type")
    private TroopType troopType;

    int remaining;

    private LocalDateTime trainingStartTime;

    private LocalDateTime trainingEndTime;

    // Getters, setters, constructors, etc.

    public Long getId() {
        return id;
    }

    public int getRemaining() {
        return remaining;
    }

    public void setRemaining(int remaining) {
        this.remaining = remaining;
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
