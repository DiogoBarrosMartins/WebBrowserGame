package com.superapi.gamerealm.model.buildings;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.superapi.gamerealm.model.Village;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
public class Building {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "village_id")// The column that holds the foreign key to the Village table
    @JsonIgnore
    private Village village;
    // Reference to the owning village

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime startedAt;
    // Represents the time when the building upgrade started

    @Temporal(TemporalType.TIMESTAMP)
    private Date timeToUpgrade;
    // Represents the time when the building will finish constructing or upgrading

    @Enumerated(EnumType.STRING)
    private BuildingType type;

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

    public boolean isResourceBuilding() {
        return type != BuildingType.PUB &&
                type != BuildingType.BARRACKS &&
                type != BuildingType.GRAIN_SILO &&
                type != BuildingType.STABLE &&
                type != BuildingType.RESEARCH_CENTER &&
                type != BuildingType.STORAGE &&
                type != BuildingType.SIEGE_WORKSHOP;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }

    public Date getTimeToUpgrade() {
        return timeToUpgrade;
    }

    public void setTimeToUpgrade(Date timeToUpgrade) {
        this.timeToUpgrade = timeToUpgrade;
    }

}
