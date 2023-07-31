package com.superapi.gamerealm.dto;

import com.superapi.gamerealm.model.buildings.BuildingType;

import java.math.BigDecimal;

public class ResourceBuildingDTO {

    private Long id;
    private BuildingType type;
    private BigDecimal productionRate;
    private int buildingLevel;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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


}
