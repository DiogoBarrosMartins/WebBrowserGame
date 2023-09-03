package com.superapi.gamerealm.model.troop;
import com.superapi.gamerealm.model.resources.TypeOfResource;

import java.util.Map;
import java.util.HashMap;

public enum TroopType {
    SCOUT(8, 1, 3, 60, 100,createResourceMap(50, 40, 20,20)),
    // health, armor, attack, trainingTime in seconds,  carryCapacity, resources
    SOLDIER(10, 2, 5, 120, 200,createResourceMap(100, 75, 40,50)),
    KNIGHT(20, 4, 10, 300,150, createResourceMap(200, 100, 150,50));


    /**
     *
     *  // Human Troops
     *     HUMAN_FOOT_TROOP(10, 2, 5, 120, 200, createResourceMap(100, 75, 40, 50), "Human foot troops are well-trained infantry."),
     *     HUMAN_ARCHER(8, 1, 6, 90, 100, createResourceMap(80, 60, 30, 40), "Human archers are skilled marksmen."),
     *     HUMAN_CAVALRY(15, 3, 8, 240, 150, createResourceMap(150, 80, 100, 60), "Human cavalry is fast and deadly."),
     *     HUMAN_SIEGE(25, 5, 12, 360, 300, createResourceMap(300, 200, 250, 100), "Human siege troops can breach any fortress."),
     *
     *     // Orc Troops
     *     ORC_FOOT_TROOP(12, 1, 6, 100, 180, createResourceMap(80, 60, 50, 30), "Orc foot troops are fearsome warriors."),
     *     ORC_ARCHER(9, 2, 4, 110, 110, createResourceMap(70, 55, 40, 25), "Orc archers shoot deadly arrows."),
     *     ORC_CAVALRY(18, 4, 9, 260, 120, createResourceMap(120, 70, 80, 40), "Orc cavalry charges with brute force."),
     *     ORC_SIEGE(30, 6, 15, 400, 280, createResourceMap(250, 150, 200, 80), "Orc siege engines smash through defenses."),
     *
     *     // Elf Troops
     *     ELF_FOOT_TROOP(8, 1, 4, 140, 220, createResourceMap(90, 70, 60, 40), "Elf foot troops are agile and precise."),
     *     ELF_ARCHER(10, 2, 8, 110, 90, createResourceMap(70, 50, 30, 35), "Elf archers have unparalleled accuracy."),
     *     ELF_CAVALRY(16, 3, 10, 280, 130, createResourceMap(130, 90, 70, 45), "Elf cavalry rides swift and silent."),
     *     ELF_SIEGE(22, 4, 14, 380, 260, createResourceMap(220, 140, 180, 75), "Elf siege engines blend nature and technology.");
     *
     *
     *
     *
     *
     *
     */

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


