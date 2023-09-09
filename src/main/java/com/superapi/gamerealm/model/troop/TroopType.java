package com.superapi.gamerealm.model.troop;
import com.superapi.gamerealm.model.resources.TypeOfResource;

import java.util.Map;
import java.util.HashMap;

public enum TroopType {


    // Human Foot Troops
    HUMAN_FOOT_SOLDIER(12, 3, 6, 120, 200, createResourceMap(100, 75, 40, 50), "Well-trained soldiers in the Human army."),
    HUMAN_IMPERIAL_GUARD(14, 4, 7, 140, 220, createResourceMap(120, 90, 50, 60), "Elite Imperial Guards loyal to the Human kingdom."),
    HUMAN_FOOT_KNIGHT(16, 5, 8, 160, 240, createResourceMap(140, 110, 60, 70), "Knights renowned for their valor."),
    HUMAN_FOOT_MAGES(14, 3, 10, 180, 180, createResourceMap(150, 100, 120, 70), "Mages wielding destructive spells for Human troops."),

    // Human Archers
    HUMAN_ARCHER_CORPS(10, 2, 8, 100, 100, createResourceMap(80, 60, 30, 40), "Elite archers skilled in precision and accuracy."),
    HUMAN_LONG_BOWMEN(12, 3, 9, 110, 120, createResourceMap(90, 70, 40, 50), "Long-range bowmen with exceptional accuracy."),
    HUMAN_CROSSBOWMEN(14, 4, 10, 130, 140, createResourceMap(100, 80, 50, 60), "Crossbowmen equipped with powerful crossbows."),

    // Human Cavalry
    HUMAN_CAVALRY_KNIGHTS(18, 5, 12, 240, 150, createResourceMap(150, 80, 100, 60), "Mighty knights mounted on powerful warhorses."),
    HUMAN_LIGHT_CAVALRY(16, 4, 11, 220, 140, createResourceMap(130, 80, 90, 50), "Swift Light Cavalry specializing in hit-and-run tactics."),
    HUMAN_ROYAL_LANCERS(20, 6, 14, 260, 160, createResourceMap(170, 100, 120, 70), "Royal Lancers with lances of exceptional reach."),

    // Human Siege Workshop
    HUMAN_SIEGE_ENGINEERS(25, 8, 15, 360, 300, createResourceMap(300, 200, 250, 100), "Engineers who operate devastating siege machinery."),
    HUMAN_CATAPULTS(30, 10, 18, 400, 340, createResourceMap(350, 220, 280, 120), "Catapults capable of hurling heavy projectiles."),
    HUMAN_TREBUCHETS(35, 12, 20, 420, 380, createResourceMap(400, 250, 320, 140), "Massive Trebuchets that can destroy fortifications."),

    // Orc Foot Troops
    ORC_WARRIORS(14, 2, 7, 110, 180, createResourceMap(80, 60, 50, 30), "Ferocious warriors from the Orc clans."),
    ORC_BRUTE_WARRIORS(16, 3, 8, 130, 200, createResourceMap(100, 70, 60, 40), "Brute Warriors known for their strength."),
    ORC_BLOODRAGE_BERSERKERS(18, 4, 9, 150, 220, createResourceMap(120, 80, 70, 50), "Berserkers fueled by bloodrage."),
    ORC_SHAMAN_WARRIORS(14, 3, 10, 170, 190, createResourceMap(140, 90, 80, 60), "Shamans who channel elemental powers in battle."),

    // Orc Archers
    ORC_SHADOW_ARCHERS(12, 3, 6, 120, 120, createResourceMap(70, 55, 40, 25), "Stealthy archers who strike from the shadows."),
    ORC_POISON_BOWMEN(14, 4, 7, 140, 140, createResourceMap(90, 70, 50, 40), "Bowmen equipped with poisoned arrows."),
    ORC_EXPLOSIVE_ARCHERS(16, 5, 8, 160, 160, createResourceMap(110, 80, 60, 50), "Archers with explosive arrowheads."),

    // Orc Cavalry
    ORC_BLOODRIDERS(20, 4, 10, 260, 120, createResourceMap(120, 70, 80, 40), "Mounted Bloodriders known for their brutality."),
    ORC_WAR_BOARS(18, 5, 11, 240, 140, createResourceMap(130, 80, 90, 50), "War Boars ridden by fierce Orc riders."),
    ORC_WOLF_RAIDERS(22, 6, 12, 280, 160, createResourceMap(150, 90, 100, 60), "Wolf Raiders on swift wolf mounts."),

    // Orc Siege Workshop
    ORC_SIEGE_GOLEMS(35, 10, 18, 420, 280, createResourceMap(250, 150, 200, 80), "Massive Siege Golems that tear down fortifications."),
    ORC_DEMOLISHERS(40, 12, 20, 440, 320, createResourceMap(280, 180, 240, 120), "Demolishers equipped with powerful wrecking balls."),
    ORC_LAVA_RAMMERS(45, 14, 22, 460, 360, createResourceMap(310, 200, 260, 140), "Lava Rams that spew molten lava on impact."),

    // Elf Foot Troops
    ELVISH_SCOUTS(10, 1, 4, 140, 220, createResourceMap(90, 70, 60, 40), "Swift and agile scouts from the Elven realm."),
    ELVISH_FOREST_ARCHERS(12, 2, 8, 110, 90, createResourceMap(70, 50, 30, 35), "Archers who are one with the forest."),
    ELVISH_ELITE_RANGERS(14, 3, 10, 130, 120, createResourceMap(80, 60, 40, 45), "Elite Rangers with unmatched accuracy."),
    ELVISH_DRUID_WARRIORS(16, 4, 12, 150, 140, createResourceMap(100, 70, 50, 50), "Druid Warriors with nature-infused abilities."),

    // Elf Archers
    ELVISH_WINDRIDER_ARCHERS(14, 3, 9, 120, 100, createResourceMap(80, 55, 40, 30), "Windrider Archers known for their speed and precision."),
    ELVISH_SILVERLEAF_BOWMEN(16, 4, 10, 140, 120, createResourceMap(90, 65, 50, 35), "Silverleaf Bowmen with silver-tipped arrows."),
    ELVISH_STORMRIDER_SNIPERS(18, 5, 12, 160, 140, createResourceMap(100, 70, 60, 40), "Stormrider Snipers with lightning-fast shots."),

    // Elf Cavalry
    ELVISH_GRYPHON_KNIGHTS(16, 4, 12, 280, 130, createResourceMap(130, 90, 70, 45), "Noble knights mounted on Gryphons."),
    ELVISH_FOREST_RIDERS(18, 5, 14, 260, 150, createResourceMap(140, 100, 80, 50), "Forest Riders on swift forest creatures."),
    ELVISH_MOONSHADOW_DRAGOONS(20, 6, 16, 240, 160, createResourceMap(150, 110, 90, 60), "Moonshadow Dragoons with lunar-powered spears."),

    // Elf Siege Workshop
    ELVISH_TREANT_SIEGE(30, 8, 20, 400, 260, createResourceMap(220, 140, 180, 75), "Ancient Treants transformed into siege engines."),
    ELVISH_EARTHEN_CATAPULTS(35, 10, 22, 420, 280, createResourceMap(240, 160, 200, 80), "Earthen Catapults with earth-shaking projectiles."),
    ELVISH_STARBREAKER_BALLISTAE(40, 12, 24, 440, 320, createResourceMap(260, 180, 220, 100), "Starbreaker Ballistae with celestial arrows.");


    private final int health;
    private final int armor;
    private final int attack;
    private final int trainingTime;
    private final int carryCapacity;
    private final Map<TypeOfResource, Double> resourcesRequired;
    private final String description;

    TroopType(int health, int armor, int attack, int trainingTime, int carryCapacity, Map<TypeOfResource, Double> resourcesRequired, String description) {
        this.health = health;
        this.armor = armor;
        this.attack = attack;
        this.trainingTime = trainingTime;
        this.carryCapacity = carryCapacity;
        this.resourcesRequired = resourcesRequired;
        this.description = description;
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

    public String getDescription() {
        return description;
    }
}


