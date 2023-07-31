package com.superapi.gamerealm.model.buildings;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.superapi.gamerealm.model.Village;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class Building {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "village_id") // The column that holds the foreign key to the Village table
    @JsonIgnore
    private Village village; // Reference to the owning village


    @Enumerated(EnumType.STRING)
    private BuildingType type;

    // Properties specific to each building type
    // For example, for resource production buildings:
    private BigDecimal productionRate;
    private int buildingLevel;
    private final int maxLevel = 10;

    public Building(BuildingType type, Village village) {
        this.village = village;
        this.type = type;
        this.buildingLevel = 0;
        this.productionRate = BigDecimal.valueOf(4);
    }

    public Building() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BuildingType getType() {
        return type;
    }


    public BigDecimal getProductionRate() {
        return productionRate;
    }

    public void setProductionRate(BigDecimal productionRate) {
        this.productionRate = productionRate;
    }


    public Village getVillage() {
        return village;
    }

    public void setVillage(Village village) {
        this.village = village;
    }

    public void setLevel(int i) {
        this.buildingLevel = i;
    }

    public int getLevel() {

        return buildingLevel;
    }

    public void setType(BuildingType type) {
        this.type = type;
    }

    public int getBuildingLevel() {
        return buildingLevel;
    }

    public void setBuildingLevel(int buildingLevel) {
        this.buildingLevel = buildingLevel;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public BigDecimal calculateProductionRate() {

        //  base production rate for level 0 <- constructor
        BigDecimal baseProductionRate = productionRate;

        // Define a rate of increase per level
        BigDecimal increasePerLevel = new BigDecimal("5");

        // Calculate the production rate based on the level
        // Return the calculated production rate
        return baseProductionRate.add(increasePerLevel.multiply(new BigDecimal(getLevel())));
    }
    public boolean isResourceBuilding() {
        return type != BuildingType.PUB &&
                type != BuildingType.BARRACKS &&
                type != BuildingType.GRAIN_SILO &&
                type != BuildingType.STABLE &&
                type != BuildingType.RESEARCH_CENTER &&
                type != BuildingType.STORAGE &&
                type != BuildingType.SIEGE_WORKSHOP;
    }
}
