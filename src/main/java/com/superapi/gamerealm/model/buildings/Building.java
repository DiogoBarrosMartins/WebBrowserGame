package com.superapi.gamerealm.model.buildings;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.superapi.gamerealm.model.Village;
import com.superapi.gamerealm.model.resources.Upgrade;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor  // Assuming this constructor exists, we'll add another one
@Entity
public class Building {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "village_id")
    @JsonIgnore
    private Village village;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime startedAt;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime timeToUpgrade;

    @Enumerated(EnumType.STRING)
    private BuildingType type;

    private BigDecimal productionRate;
    private int buildingLevel;
    private final int maxLevel = 10;

    public Building() {
    }


// Existing constructors and methods...

    // New constructor for initializing with BuildingType and Village
    public Building(BuildingType type, Village village) {
        this.type = type;
        this.village = village;
        this.startedAt = LocalDateTime.now();  // You might want to set a default value for startedAt
        this.timeToUpgrade = null;  // Assuming this should be set later when upgrading
        this.productionRate = BigDecimal.ZERO;  // Default production rate
        this.buildingLevel = 0;  // Initial level
    }
    public BigDecimal getNextLevelProductionRate() {
        int nextLevel = buildingLevel + 1;
        if (nextLevel >= 0 && nextLevel < Upgrade.RESOURCE_BUILDING_PRODUCTION_RATES.length) {
            return BigDecimal.valueOf(Upgrade.RESOURCE_BUILDING_PRODUCTION_RATES[nextLevel]);
        } else {
            return productionRate;
        }
    }

    public boolean isResourceBuilding() {
        return type != BuildingType.BARRACKS &&
                type != BuildingType.WALLS &&
                type != BuildingType.ARCHERY_RANGE;
    }

    @Override
    public String toString() {
        return "Building{" +
                "id=" + id +
                ", village=" + village +
                ", startedAt=" + startedAt +
                ", timeToUpgrade=" + timeToUpgrade +
                ", type=" + type +
                ", productionRate=" + productionRate +
                ", buildingLevel=" + buildingLevel +
                ", maxLevel=" + maxLevel +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Village getVillage() {
        return village;
    }

    public void setVillage(Village village) {
        this.village = village;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }



    public BuildingType getType() {
        return type;
    }

    public void setType(BuildingType type) {
        this.type = type;
    }

    public BigDecimal getProductionRate() {
        return productionRate;
    }

    public void setProductionRate(BigDecimal productionRate) {
        this.productionRate = productionRate;
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

    public LocalDateTime getTimeToUpgrade() {
        return timeToUpgrade;
    }

    public void setTimeToUpgrade(LocalDateTime timeToUpgrade) {
        this.timeToUpgrade = timeToUpgrade;
    }
}
