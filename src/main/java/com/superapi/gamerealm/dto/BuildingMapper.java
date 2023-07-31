package com.superapi.gamerealm.dto;

import com.superapi.gamerealm.model.buildings.Building;
import org.springframework.stereotype.Component;

@Component
public class BuildingMapper {
    public static ResourceBuildingDTO toResourceBuildingDTO(Building building) {
        ResourceBuildingDTO dto = new ResourceBuildingDTO();
        dto.setId(building.getId());
        dto.setType(building.getType());
        dto.setProductionRate(building.getProductionRate());
        dto.setBuildingLevel(building.getLevel());
        return dto;
    }
    public static NonResourceBuildingDTO toNonResourceBuildingDTO(Building building) {
        NonResourceBuildingDTO dto = new NonResourceBuildingDTO();
        dto.setId(building.getId());
        dto.setType(building.getType());
        dto.setBuildingLevel(building.getLevel());
        return dto;
    }
}
