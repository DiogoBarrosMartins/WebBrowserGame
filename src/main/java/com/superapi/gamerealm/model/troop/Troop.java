package com.superapi.gamerealm.model.troop;


import com.superapi.gamerealm.model.Village;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Troop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime finishTrainingTime;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "village_id")
    private Village village;
    @Enumerated(EnumType.STRING)
    private TroopType type;

    private int level;

    private int trainingTime;

    private int[] resourcesNeeded;

    private int attack;

    private int defense;

    private int health;

    public Troop(Long id, TroopType type, int level, int trainingTime, int[] resourcesNeeded, int attack, int defense, int health) {
        this.id = id;
        this.type = type;
        this.level = level;
        this.trainingTime = trainingTime;
        this.resourcesNeeded = resourcesNeeded;
        this.attack = attack;
        this.defense = defense;
        this.health = health;
    }

    public Troop() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TroopType getType() {
        return type;
    }

    public void setType(TroopType type) {
        this.type = type;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getTrainingTime() {
        return trainingTime;
    }

    public void setTrainingTime(int trainingTime) {
        this.trainingTime = trainingTime;
    }
    public boolean isTrained() {
        return LocalDateTime.now().isAfter(finishTrainingTime);
    }

    public void setFinishTrainingTime(LocalDateTime finishTime) {
        this.finishTrainingTime = finishTime;
    }
    public int[] getResourcesNeeded() {
        return resourcesNeeded;
    }

    public void setResourcesNeeded(int[] resourcesNeeded) {
        this.resourcesNeeded = resourcesNeeded;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }
}

