package com.superapi.gamerealm.controller;

import com.superapi.gamerealm.dto.VillageDTO;
import com.superapi.gamerealm.dto.VillageMapper;
import com.superapi.gamerealm.model.buildings.Building;
import com.superapi.gamerealm.service.BuildingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/{villageId}/buildings/{buildingId}")
public class UpgradeController {
    private final BuildingService buildingService;
    private final VillageMapper villageMapper;

    @Autowired
    public UpgradeController(BuildingService buildingService, VillageMapper villageMapper) {
        this.buildingService = buildingService;
        this.villageMapper = villageMapper;
    }

    @PutMapping("/upgrade")
    public ResponseEntity<VillageDTO> upgradeBuilding(@PathVariable Long villageId, @PathVariable Long buildingId) {
        System.out.println("Upgrade called for building ID: " + buildingId + " in village ID: " + villageId);

        Building building = buildingService.findById(buildingId);
        if (building == null || building.getVillage() == null || building.getVillage().getId() != villageId) {
            System.out.println("Building not found or does not belong to the specified village");
            return ResponseEntity.notFound().build();
        }

        try {
            Building upgradedBuilding = buildingService.upgradeBuilding(building.getId());
            VillageDTO villageDTO = villageMapper.villageToVillageDTO(upgradedBuilding.getVillage());
            return ResponseEntity.ok(villageDTO);
        } catch (IllegalStateException e) {
            System.out.println("Upgrade failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        } catch (Exception e) {
            System.out.println("Unexpected error during upgrade: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}

