package com.superapi.gamerealm.dto;

import com.superapi.gamerealm.model.buildings.BuildingType;

import java.math.BigDecimal;

public class NonResourceBuildingDTO  {
    private Long id;
    private BuildingType type;
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





    public int getBuildingLevel() {
        return buildingLevel;
    }

    public void setBuildingLevel(int buildingLevel) {
        this.buildingLevel = buildingLevel;
    }

}
