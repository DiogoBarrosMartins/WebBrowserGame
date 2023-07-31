package com.superapi.gamerealm.controller;

import com.superapi.gamerealm.dto.NonResourceBuildingDTO;
import com.superapi.gamerealm.dto.ResourceBuildingDTO;
import com.superapi.gamerealm.model.buildings.Building;
import com.superapi.gamerealm.service.BuildingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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


    @PutMapping("/{buildingId}/upgrade")
    public ResponseEntity<Building> upgradeBuilding(@PathVariable Long buildingId) {
        Building upgradedBuilding = buildingService.upgradeBuilding(buildingId);
        return ResponseEntity.ok(upgradedBuilding);
    }
    // Add more endpoints as needed (e.g., upgrade building, delete building, etc.)
}
