package com.superapi.gamerealm.controller;

import com.superapi.gamerealm.dto.VillageDTO;
import com.superapi.gamerealm.dto.VillageMapper;
import com.superapi.gamerealm.model.buildings.Building;
import com.superapi.gamerealm.service.BuildingService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<VillageDTO> upgradeBuilding(@PathVariable Long buildingId) {
        System.out.println("upgrade called" );
        Building building = buildingService.findById(buildingId);
        if (building == null) {
            return ResponseEntity.notFound().build();
        }
        try {
            Building upgradedBuilding = buildingService.upgradeBuilding(building);
            // Assuming you have a service or mapper to convert a Village entity to VillageDTO
            VillageDTO villageDTO = villageMapper.villageToVillageDTO(upgradedBuilding.getVillage());
            return ResponseEntity.ok(villageDTO);
        } catch (IllegalStateException e) {
            return null;
        }
    }
}

