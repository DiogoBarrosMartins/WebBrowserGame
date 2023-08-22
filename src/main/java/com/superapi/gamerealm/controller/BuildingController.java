package com.superapi.gamerealm.controller;

import com.superapi.gamerealm.dto.NonResourceBuildingDTO;
import com.superapi.gamerealm.dto.ResourceBuildingDTO;
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



}
