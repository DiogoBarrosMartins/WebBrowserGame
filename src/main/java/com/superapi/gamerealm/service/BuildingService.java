package com.superapi.gamerealm.service;

import com.superapi.gamerealm.model.Village;
import com.superapi.gamerealm.model.buildings.Building;
import com.superapi.gamerealm.repository.BuildingRepository;
import com.superapi.gamerealm.repository.VillageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BuildingService {
    private final BuildingRepository buildingRepository;
    private final VillageRepository villageRepository;

    public BuildingService(BuildingRepository buildingRepository, VillageRepository villageRepository) {
        this.buildingRepository = buildingRepository;
        this.villageRepository = villageRepository;
    }

    public Building createBuilding(Long villageId, Building building) {
        Village village = villageRepository.findById(villageId)
                .orElseThrow(() -> new IllegalArgumentException("Village not found with ID: " + villageId));
        building.setVillage(village);
        return buildingRepository.save(building);
    }

    public List<Building> getAllBuildingsInVillage(Long villageId) {
        return buildingRepository.findByVillageId(villageId);
    }

    public Building upgradeBuilding(Long buildingId) {
        Building building = buildingRepository.findById(buildingId)
                .orElseThrow(() -> new IllegalArgumentException("Building not found with ID: " + buildingId));

        // Check if the building's level is less than the maximum level (e.g., 3) before upgrading
        if (building.getLevel() < building.getMaxLevel()) {
            building.setLevel(building.getLevel() + 1);
            return buildingRepository.save(building);
        } else {
            throw new IllegalStateException("Building is already at the maximum level.");
        }
    }

    // Add more methods as needed (e.g., upgradeBuilding, deleteBuilding, etc.)
}
