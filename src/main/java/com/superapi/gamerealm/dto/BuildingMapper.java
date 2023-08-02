package com.superapi.gamerealm.dto;

import com.superapi.gamerealm.model.buildings.Building;
import com.superapi.gamerealm.model.resources.Upgrade;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class BuildingMapper {
    public static ResourceBuildingDTO toResourceBuildingDTO(Building building) {
        return new ResourceBuildingDTO(
                building.getId(),
                building.getType(),
                building.getLevel(),
                building.calculateProductionRate(),
                building.getMaxLevel(),
                Upgrade.getResourceBuildingResourcesNeeded(building.getType().toString(), building.getLevel())
        );
    }

    public static NonResourceBuildingDTO toNonResourceBuildingDTO(Building building) {
        return new NonResourceBuildingDTO(
                building.getId(),
                building.getType(),
                building.getLevel(),
                building.getMaxLevel(),
                Upgrade.getNonResourceBuildingResourcesNeeded(building.getType().toString(), building.getLevel())
        );
    }


}
