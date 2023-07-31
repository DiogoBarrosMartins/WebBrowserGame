package com.superapi.gamerealm.service;

import com.superapi.gamerealm.dto.BuildingMapper;
import com.superapi.gamerealm.dto.NonResourceBuildingDTO;
import com.superapi.gamerealm.dto.ResourceBuildingDTO;
import com.superapi.gamerealm.model.buildings.Building;
import com.superapi.gamerealm.model.buildings.BuildingType;
import com.superapi.gamerealm.repository.BuildingRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BuildingService {
    private final BuildingRepository buildingRepository;

    public BuildingService(BuildingRepository buildingRepository) {
        this.buildingRepository = buildingRepository;
    }

    public List<ResourceBuildingDTO> getAllResourceBuildingsInVillage(Long villageId) {
        List<Building> buildings = buildingRepository.findByVillageId(villageId);
        for (Building building : buildings) {
            building.calculateProductionRate();
        }
        return buildings.stream()
                .filter(building -> building.getType() == BuildingType.FARM
                        || building.getType() == BuildingType.QUARRY
                        || building.getType() == BuildingType.MINE
                        || building.getType() == BuildingType.FOREST)

                .map(BuildingMapper::toResourceBuildingDTO)
                .collect(Collectors.toList());
    }

    public List<NonResourceBuildingDTO> getAllNonResourceBuildingsInVillage(Long villageId) {
        List<Building> buildings = buildingRepository.findByVillageId(villageId);
        return buildings.stream()
                .filter(building -> building.getType() != BuildingType.FARM
                        && building.getType() != BuildingType.QUARRY
                        && building.getType() != BuildingType.MINE
                        && building.getType() != BuildingType.FOREST)
                .map(BuildingMapper::toNonResourceBuildingDTO)
                .collect(Collectors.toList());
    }


    public Building upgradeBuilding(Long buildingId) {
        Building building = buildingRepository.findById(buildingId)
                .orElseThrow(() -> new IllegalArgumentException("Building not found with ID: " + buildingId));

        // Check if the building's level is less than the maximum level (e.g., 3) before upgrading
        if (building.getLevel() < building.getMaxLevel()) {
            building.setLevel(building.getLevel() + 1);
            if (building.isResourceBuilding()) {
                updateResourceBuildingProductionRate(building);
            }
            return buildingRepository.save(building);
        } else {
            throw new IllegalStateException("Building is already at the maximum level.");
        }
    }

    private void updateResourceBuildingProductionRate(Building building) {
        // Calculate the production rate based on the new level and update the building
        BigDecimal productionRate = building.calculateProductionRate();
        building.setProductionRate(productionRate);
    }
}
