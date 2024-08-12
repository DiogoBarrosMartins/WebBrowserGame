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

    @Setter
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



    public Building(BuildingType type, Village village) {
        this.type = type;
        this.village = village;
        this.startedAt = LocalDateTime.now();
        this.timeToUpgrade = null;
        this.productionRate = BigDecimal.ZERO;
        this.buildingLevel = 0;
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

    public LocalDateTime getTimeToUpgrade() {
        return  LocalDateTime.now().plusMinutes(Upgrade.RESOURCE_BUILDING_PRODUCTION_RATES[buildingLevel]);
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

}
