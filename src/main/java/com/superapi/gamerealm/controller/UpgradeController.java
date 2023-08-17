package com.superapi.gamerealm.controller;

import com.superapi.gamerealm.dto.BuildingMapper;
import com.superapi.gamerealm.dto.NonResourceBuildingDTO;
import com.superapi.gamerealm.dto.ResourceBuildingDTO;
import com.superapi.gamerealm.model.buildings.Building;
import com.superapi.gamerealm.service.BuildingService;
import com.superapi.gamerealm.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/{villageId}/buildings/{buildingId}")
public class UpgradeController {
    private final BuildingService buildingService;
    private final ResourceService resourceService;

    @Autowired
    public UpgradeController(BuildingService buildingService, ResourceService resourceService) {
        this.buildingService = buildingService;
        this.resourceService = resourceService;
    }

    @PostMapping("/upgrade")
    public ResponseEntity<?> upgradeBuilding(  @PathVariable Long buildingId) {
        try {
            Building building = buildingService.upgradeBuilding(buildingId);
            resourceService.updateVillageResources(building.getVillage());

            if (building.isResourceBuilding()) {
                ResourceBuildingDTO buildingDTO = BuildingMapper.toResourceBuildingDTO(building);
                return ResponseEntity.ok(buildingDTO);
            } else {
                NonResourceBuildingDTO buildingDTO = BuildingMapper.toNonResourceBuildingDTO(building);
                return ResponseEntity.ok(buildingDTO);
            }
        } catch (IllegalStateException ex) {
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
