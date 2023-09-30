package com.superapi.gamerealm.model.troop;

import com.superapi.gamerealm.model.resources.TypeOfResource;

import java.util.Map;

public abstract class Troop {
    private int health;
    private int armor;
    private int attack;
    private int trainingTime;
    private int carryCapacity;
    private Map<TypeOfResource, Double> resourcesRequired;
    private String description;
    private TroopType.TroopCategory category;

    // Constructor
    public Troop(int health, int armor, int attack, int trainingTime, int carryCapacity,
                 Map<TypeOfResource, Double> resourcesRequired, String description, TroopType.TroopCategory category) {
        this.health = health;
        this.armor = armor;
        this.attack = attack;
        this.trainingTime = trainingTime;
        this.carryCapacity = carryCapacity;
        this.resourcesRequired = resourcesRequired;
        this.description = description;
        this.category = category;
    }

    // Getters and Setters for all attributes
    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getArmor() {
        return armor;
    }

    public void setArmor(int armor) {
        this.armor = armor;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getTrainingTime() {
        return trainingTime;
    }

    public void setTrainingTime(int trainingTime) {
        this.trainingTime = trainingTime;
    }

    public int getCarryCapacity() {
        return carryCapacity;
    }

    public void setCarryCapacity(int carryCapacity) {
        this.carryCapacity = carryCapacity;
    }

    public Map<TypeOfResource, Double> getResourcesRequired() {
        return resourcesRequired;
    }

    public void setResourcesRequired(Map<TypeOfResource, Double> resourcesRequired) {
        this.resourcesRequired = resourcesRequired;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TroopType.TroopCategory getCategory() {
        return category;
    }

    public void setCategory(TroopType.TroopCategory category) {
        this.category = category;
    }

    // Common behaviors (methods)
    public void move() {
        // Implement common move logic here
    }

    public void attack() {
        // Implement common attack logic here
    }
}
