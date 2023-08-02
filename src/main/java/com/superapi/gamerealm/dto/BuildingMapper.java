package com.superapi.gamerealm.dto;

import com.superapi.gamerealm.model.buildings.Building;
import com.superapi.gamerealm.model.buildings.BuildingType;
import com.superapi.gamerealm.model.resources.Upgrade;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class BuildingMapper {
    public static ResourceBuildingDTO toResourceBuildingDTO(Building building) {
        ResourceBuildingDTO dto = new ResourceBuildingDTO();
        dto.setId(building.getId());
        dto.setType(building.getType());
        dto.setLevel(building.getLevel());
        dto.setProductionRate(building.getProductionRate());
        dto.setMaxLevel(building.getMaxLevel());

        // Set the resourcesNeeded attribute based on the building type and level
        String buildingTypeString = building.getType().toString();
        int[] resourcesNeeded = Upgrade.getResourceBuildingResourcesNeeded(buildingTypeString, building.getLevel());
        dto.setResourcesNeeded(resourcesNeeded);

        return dto;
    }


    public static NonResourceBuildingDTO toNonResourceBuildingDTO(Building building) {
        int level = building.getLevel();
        int maxLevel = building.getMaxLevel();
        BuildingType type = building.getType();
        int[] resourcesNeeded = Upgrade.getNonResourceBuildingResourcesNeeded(type.toString(), level);

        return new NonResourceBuildingDTO(
                building.getId(),
                type,
                level,
                maxLevel,
                resourcesNeeded,
                // You need to set the correct value for the timeLeft attribute based on your requirements
                // For now, let's set it to 0 as you have in the NonResourceBuildingDTO constructor
                0
        );
    }


}
