package com.superapi.gamerealm.dto;

import com.superapi.gamerealm.model.buildings.Building;
import org.springframework.stereotype.Component;

@Component
public class BuildingMapper {

    public BuildingDTO toDTO(Building building) {
        BuildingDTO dto = new BuildingDTO();
        dto.setId(building.getId());
        dto.setType(building.getType());
        dto.setBuildingLevel(building.getLevel());
        return dto;
    }
}
