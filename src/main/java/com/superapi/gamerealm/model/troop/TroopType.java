package com.superapi.gamerealm.model.troop;
import com.superapi.gamerealm.model.resources.TypeOfResource;

import java.util.Map;
import java.util.HashMap;

public enum TroopType {
    INFANTRY(10, 1, 3, 60, 100,createResourceMap(10, 5, 0,20)), // health, armor, attack, carryCapacity, resources
    ARCHER(8, 1, 5, 120, 150,createResourceMap(8, 4, 2,10)),
    CAVALRY(15, 2, 4, 240,200, createResourceMap(15, 7, 3,40));

    private final int health;
    private final int armor;
    private final int attack;
    private final int carryCapacity;
    private int trainingTime;
    private final Map<TypeOfResource, Double> resourcesRequired;

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

public String getName(){
        return this.name();
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
}


