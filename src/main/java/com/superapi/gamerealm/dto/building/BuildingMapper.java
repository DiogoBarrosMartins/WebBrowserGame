package com.superapi.gamerealm.dto.building;

import com.superapi.gamerealm.model.buildings.Building;
import com.superapi.gamerealm.model.buildings.BuildingType;
import com.superapi.gamerealm.model.resources.Upgrade;
import org.springframework.stereotype.Component;

@Component
public class BuildingMapper {

    public static ResourceBuildingDTO toResourceBuildingDTO(Building building) {
        ResourceBuildingDTO dto = new ResourceBuildingDTO();
        dto.setId(building.getId());
        dto.setType(building.getType());
        dto.setLevel(building.getBuildingLevel());
        dto.setProductionRate(building.getProductionRate());
        dto.setMaxLevel(building.getMaxLevel());
        dto.setNextLevelProductionRate(building.getNextLevelProductionRate());
        dto.setTimeToUpgrade(Upgrade.RESOURCE_BUILDING_UPGRADE_TIMES[building.getBuildingLevel()]);
        dto.setResourcesNeeded(getResourcesNeeded(building.getType(), building.getBuildingLevel()));

        return dto;
    }

    public static NonResourceBuildingDTO toNonResourceBuildingDTO(Building building) {
        int[] resourcesNeeded = Upgrade.getResourcesNeeded(BuildingType.valueOf(building.getType().toString()), building.getBuildingLevel());

        return new NonResourceBuildingDTO(
                building.getId(),
                building.getType(),
                building.getBuildingLevel(),
                building.getMaxLevel(),
                resourcesNeeded,
                0 // Placeholder for timeLeft, adjust as per your application logic
        );
    }

    private static int[] getResourcesNeeded(BuildingType type, int level) {
        return Upgrade.getResourcesNeeded(type,level);

    }
}
