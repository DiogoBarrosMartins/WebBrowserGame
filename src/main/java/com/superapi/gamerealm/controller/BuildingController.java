package com.superapi.gamerealm.controller;

import com.superapi.gamerealm.dto.building.NonResourceBuildingDTO;
import com.superapi.gamerealm.dto.building.ResourceBuildingDTO;
import com.superapi.gamerealm.dto.troops.BuildingTroopInfoDTO;
import com.superapi.gamerealm.model.buildings.Building;
import com.superapi.gamerealm.service.BuildingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("buildings/{villageId}/")
public class BuildingController {
    private final BuildingService buildingService;

    public BuildingController(BuildingService buildingService) {
        this.buildingService = buildingService;
    }

    @GetMapping("/resource")
    public ResponseEntity<List<ResourceBuildingDTO>> getAllResourceBuildingsInVillage(@PathVariable Long villageId) {
        List<ResourceBuildingDTO> resourceBuildings = buildingService.getAllResourceBuildingsInVillage(villageId);
        return ResponseEntity.ok(resourceBuildings);
    }

    @GetMapping("/non-resource")
    public ResponseEntity<List<NonResourceBuildingDTO>> getAllNonResourceBuildingsInVillage(@PathVariable Long villageId) {
        List<NonResourceBuildingDTO> nonResourceBuildings = buildingService.getAllNonResourceBuildingsInVillage(villageId);
        return ResponseEntity.ok(nonResourceBuildings);
    }
    @GetMapping("/{buildingId}/buildable-troops")
    public ResponseEntity<BuildingTroopInfoDTO> getBuildableTroopsForBuilding(
            @PathVariable Long buildingId
    ) {
        // Fetch the building based on villageId and buildingId
        Building building = buildingService.findById(buildingId);

        if (building == null) {
            return ResponseEntity.notFound().build();
        }

        // Determine the buildable troops for the building
        List<String> buildableTroops = buildingService.getBuildableTroopsForBuilding(building);

        BuildingTroopInfoDTO troopInfoDTO = new BuildingTroopInfoDTO();
        troopInfoDTO.setBuildingType(building.getType());
        troopInfoDTO.setBuildableTroops(buildableTroops);

        return ResponseEntity.ok(troopInfoDTO);
    }
}


}
