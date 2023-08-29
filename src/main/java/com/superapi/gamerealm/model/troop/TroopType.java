package com.superapi.gamerealm.model.troop;

public enum TroopType {

    FOOT_SOLDIER_TIER1("Foot Soldier Tier 1", 100, 10, 5, 20),
    FOOT_SOLDIER_TIER2("Foot Soldier Tier 2", 150, 15, 7, 30),
    FOOT_SOLDIER_TIER3("Foot Soldier Tier 3", 200, 20, 10, 40);

    private final String name;
    private final int health;
    private final int attack;
    private final int armor;
    private final int carryCapacity;

    TroopType(String name, int health, int attack, int armor, int carryCapacity) {
        this.name = name;
        this.health = health;
        this.attack = attack;
        this.armor = armor;
        this.carryCapacity = carryCapacity;
    }

    public String getName() {
        return name;
    }

    public int getHealth() {
        return health;
    }

    public int getAttack() {
        return attack;
    }

    public int getArmor() {
        return armor;
    }

    public int getCarryCapacity() {
        return carryCapacity;
    }
}
