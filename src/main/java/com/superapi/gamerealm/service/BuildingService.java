package com.superapi.gamerealm.service;

import com.superapi.gamerealm.dto.BuildingMapper;
import com.superapi.gamerealm.dto.NonResourceBuildingDTO;
import com.superapi.gamerealm.dto.ResourceBuildingDTO;
import com.superapi.gamerealm.model.buildings.Building;
import com.superapi.gamerealm.model.buildings.BuildingType;
import com.superapi.gamerealm.model.resources.TypeOfResource;
import com.superapi.gamerealm.model.resources.Upgrade;
import com.superapi.gamerealm.repository.BuildingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BuildingService {
    private final BuildingRepository buildingRepository;
    private final ResourceService resourceService;

    @Autowired
    public BuildingService(BuildingRepository buildingRepository, ResourceService resourceService) {
        this.buildingRepository = buildingRepository;
        this.resourceService = resourceService;
    }

    public List<ResourceBuildingDTO> getAllResourceBuildingsInVillage(Long villageId) {
        List<Building> buildings = buildingRepository.findByVillageId(villageId);

        // Calculate production rates for all buildings
        for (Building building : buildings) {
            BigDecimal productionRate = resourceService.calculateProductionRate(building);
            building.setProductionRate(productionRate);
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

        // Check if the building's level is less than the maximum level (e.g., 10) before upgrading
        if (building.getLevel() >= building.getMaxLevel()) {
            throw new IllegalStateException("Building is already at the maximum level.");
        }

        // Check if the player has enough resources to upgrade the building
        Map<TypeOfResource, Double> resourcesNeeded = Upgrade.getResourceNeeded(building.getType(), building.getLevel());

        if (resourceService.hasEnoughResources(building.getVillage().getId(), resourcesNeeded)) {
            // Deduct the required resources from the player's inventory
            resourceService.deductResources(building.getVillage().getId(), resourcesNeeded);

            // Set the startedAt field to the current date and time
            building.setStartedAt(LocalDateTime.now());

            // Increase the building's level
            building.setLevel(building.getLevel() + 1);

            // Set the time to upgrade to the appropriate value
            calculateUpgradeTime(building.getType(), building.getLevel() + 1);

            return buildingRepository.save(building);
        } else {
            throw new IllegalStateException("Player doesn't have enough resources to upgrade this building.");
        }
    }



    private Date calculateUpgradeTime(BuildingType buildingType, int nextLevel) {
        int[] upgradeTimes = Upgrade.RESOURCE_BUILDING_UPGRADE_TIMES;
        if (nextLevel <= upgradeTimes.length) {
            // Get the upgrade time in minutes for the next level
            int upgradeTimeMinutes = upgradeTimes[nextLevel - 1];
            // Calculate the time when the upgrade will be completed
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.MINUTE, upgradeTimeMinutes);
            return calendar.getTime();
        } else {
            throw new IllegalStateException("Building is already at the maximum level.");
        }
    }




}
