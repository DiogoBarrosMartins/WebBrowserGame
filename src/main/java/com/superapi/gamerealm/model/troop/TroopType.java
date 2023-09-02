package com.superapi.gamerealm.model.troop;
import com.superapi.gamerealm.model.resources.TypeOfResource;

import java.util.Map;
import java.util.HashMap;

public enum TroopType {
    SCOUT(8, 1, 3, 60, 100,createResourceMap(50, 40, 20,20)),
    // health, armor, attack, trainingTime in seconds,  carryCapacity, resources
    SOLDIER(10, 2, 5, 120, 200,createResourceMap(100, 75, 40,50)),
    KNIGHT(20, 4, 10, 300,150, createResourceMap(200, 100, 150,50));

    private final int health;
    private final int armor;
    private final int attack;
    private final int carryCapacity;
    private int trainingTime;
    private final Map<TypeOfResource, Double> resourcesRequired;
private String name;
    TroopType(int health, int armor, int attack, int trainingTime, int carryCapacity, Map<TypeOfResource, Double> resourcesRequired) {
        this.health = health;
        this.armor = armor;
        this.attack = attack;
        this.trainingTime = trainingTime;
        this.carryCapacity = carryCapacity;
        this.resourcesRequired = resourcesRequired;
    }

    private static Map<TypeOfResource, Double> createResourceMap(double wood, double wheat, double stone, double gold) {
        Map<TypeOfResource, Double> resourceMap = new HashMap<>();
        resourceMap.put(TypeOfResource.WOOD, wood);
        resourceMap.put(TypeOfResource.WHEAT, wheat);
        resourceMap.put(TypeOfResource.STONE, stone);
        resourceMap.put(TypeOfResource.GOLD, gold);
        return resourceMap;
    }


    public int getTrainingTime() {
        return trainingTime;
    }

    public int getHealth() {
        return health;
    }

    public int getArmor() {
        return armor;
    }

    public int getAttack() {
        return attack;
    }

    public int getCarryCapacity() {
        return carryCapacity;
    }

    public Map<TypeOfResource, Double> getResourcesRequired() {
        return resourcesRequired;
    }


    public String getName() {
        return this.name().substring(0, 1).toUpperCase() + this.name().substring(1).toLowerCase();
    }

}


