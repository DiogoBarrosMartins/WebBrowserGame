package com.superapi.gamerealm.controller;

import com.superapi.gamerealm.model.buildings.Building;
import com.superapi.gamerealm.service.BuildingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/{villageId}/buildings")
public class BuildingController {
    private final BuildingService buildingService;

    public BuildingController(BuildingService buildingService) {
        this.buildingService = buildingService;
    }

    @PostMapping
    public ResponseEntity<Building> createBuilding(@PathVariable Long villageId, @RequestBody Building building) {
        Building createdBuilding = buildingService.createBuilding(villageId, building);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBuilding);
    }

    @GetMapping
    public ResponseEntity<List<Building>> getAllBuildingsInVillage(@PathVariable Long villageId) {
        List<Building> buildings = buildingService.getAllBuildingsInVillage(villageId);
        return ResponseEntity.ok(buildings);
    }
    @PutMapping("/{buildingId}/upgrade")
    public ResponseEntity<Building> upgradeBuilding(@PathVariable Long buildingId) {
        Building upgradedBuilding = buildingService.upgradeBuilding(buildingId);
        return ResponseEntity.ok(upgradedBuilding);
    }
    // Add more endpoints as needed (e.g., upgrade building, delete building, etc.)
}
