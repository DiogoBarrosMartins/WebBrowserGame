package com.superapi.gamerealm.dto.troops;

import com.superapi.gamerealm.model.resources.TypeOfResource;
import com.superapi.gamerealm.model.troop.TroopType;

import java.util.Map;
public class TroopTypeDTO {
    private String name;
    private int health;
    private int armor;
    private int attack;
    private int carryCapacity;
    private int trainingTime;
    private Map<TypeOfResource, Double> resourcesRequired;
    private String description;

    public TroopTypeDTO(TroopType troopType) {
        this.name = troopType.name();
        this.health = troopType.getHealth();
        this.armor = troopType.getArmor();
        this.attack = troopType.getAttack();
        this.carryCapacity = troopType.getCarryCapacity();
        this.trainingTime = troopType.getTrainingTime();
        this.resourcesRequired = troopType.getResourcesRequired();
        this.description = troopType.getDescription();
    }

    public TroopTypeDTO(String name, int health, int armor, int attack, int carryCapacity, int trainingTime, Map<TypeOfResource, Double> resourcesRequired, String description) {
        this.name = name;
        this.health = health;
        this.armor = armor;
        this.attack = attack;
        this.carryCapacity = carryCapacity;
        this.trainingTime = trainingTime;
        this.resourcesRequired = resourcesRequired;
        this.description = description;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public int getCarryCapacity() {
        return carryCapacity;
    }

    public void setCarryCapacity(int carryCapacity) {
        this.carryCapacity = carryCapacity;
    }

    public int getTrainingTime() {
        return trainingTime;
    }

    public void setTrainingTime(int trainingTime) {
        this.trainingTime = trainingTime;
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
// Getters and setters (if needed) for the fields

    // Additional methods if necessary
}
