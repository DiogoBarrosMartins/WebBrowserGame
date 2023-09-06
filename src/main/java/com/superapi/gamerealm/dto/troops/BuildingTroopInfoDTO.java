package com.superapi.gamerealm.dto.troops;

import java.util.List;

public class BuildingTroopInfoDTO {
    private String buildingType;
    private List<String> buildableTroops;

    // Constructors, getters, and setters

    public BuildingTroopInfoDTO(String buildingType, List<String> buildableTroops) {
        this.buildingType = buildingType;
        this.buildableTroops = buildableTroops;
    }

    public BuildingTroopInfoDTO() {
    }

    public String getBuildingType() {
        return buildingType;
    }

    public void setBuildingType(String buildingType) {
        this.buildingType = buildingType;
    }

    public List<String> getBuildableTroops() {
        return buildableTroops;
    }

    public void setBuildableTroops(List<String> buildableTroops) {
        this.buildableTroops = buildableTroops;
    }
}

