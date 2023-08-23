package com.superapi.gamerealm.service;

import com.superapi.gamerealm.dto.BuildingMapper;
import com.superapi.gamerealm.dto.NonResourceBuildingDTO;
import com.superapi.gamerealm.dto.ResourceBuildingDTO;
import com.superapi.gamerealm.model.buildings.Building;
import com.superapi.gamerealm.model.buildings.BuildingType;
import com.superapi.gamerealm.model.buildings.Construction;
import com.superapi.gamerealm.model.resources.TypeOfResource;
import com.superapi.gamerealm.model.resources.Upgrade;
import com.superapi.gamerealm.repository.BuildingRepository;
import com.superapi.gamerealm.repository.ConstructionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class BuildingService {
    private final BuildingRepository buildingRepository;
    private final ResourceService resourceService;

    private final Logger logger = LoggerFactory.getLogger(BuildingService.class);

    private final ConstructionRepository constructionRepository;

    @Autowired
    public BuildingService(BuildingRepository buildingRepository, ConstructionRepository constructionRepository, ResourceService resourceService) {
        this.buildingRepository = buildingRepository;
        this.constructionRepository = constructionRepository;
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

    public Building upgradeBuilding(Building building) {

        System.out.println(building.toString());

        for (Construction c : constructionRepository.findAll()) {
            System.out.println(c.toString());
        }

        Construction existingConstruction = constructionRepository.findByBuildingId(building.getId());
        if (existingConstruction != null) {
            // Handle the situation, e.g., throw an exception with a custom message
            throw new IllegalStateException("An upgrade is already in progress for this building.");
        }

        // Check if the building can be upgraded
        if (building.getLevel() >= building.getMaxLevel()) {
            throw new IllegalStateException("Building is already at the maximum level.");
        }

        // Check if the player has enough resources to upgrade the building
        Map<TypeOfResource, Double> resourcesNeeded = Upgrade.getResourceNeeded(building.getType(), building.getLevel());

        if (resourceService.hasEnoughResources(building.getVillage().getId(), resourcesNeeded)) {
            // Deduct the required resources from the player's inventory
            resourceService.deductResources(building.getVillage().getId(), resourcesNeeded);

            Construction construction = new Construction();
            construction.setBuilding(building);
            construction.setVillage(building.getVillage());

            // Calculate the upgrade time and set the construction's startedAt and endsAt
            Double upgradeTimeInMinutes = Upgrade.RESOURCE_BUILDING_UPGRADE_TIMES[building.getLevel() + 1];
            LocalDateTime now = LocalDateTime.now();
            construction.setStartedAt(now);
            construction.setEndsAt(now.plusMinutes(upgradeTimeInMinutes.longValue()));

            // Calculate the Duration between startedAt and endsAt
            Duration upgradeDuration = Duration.between(now, construction.getEndsAt());
            // Convert the Duration to the expected format for timeToUpgrade (assuming it's a Date)
            Date timeToUpgradeDate = Date.from(now.plus(upgradeDuration).atZone(ZoneId.systemDefault()).toInstant());
            building.setTimeToUpgrade(timeToUpgradeDate);

            building.getVillage().getConstructions().add(construction);

            // Save the construction item to the repository
            constructionRepository.save(construction);
            buildingRepository.save(building);
            // Schedule a task to be executed when the construction completes
            scheduleConstructionCompletion(construction);

            return building;

        } else {
            throw new IllegalStateException("Player doesn't have enough resources to upgrade this building.");
        }
    }



    private void scheduleConstructionCompletion(Construction construction) {
        long delay = Duration.between(LocalDateTime.now(), construction.getEndsAt()).toMillis();
        CompletableFuture.delayedExecutor(delay, TimeUnit.MILLISECONDS)
                .execute(() -> completeConstruction(construction));
    }

    private void completeConstruction(Construction construction) {
        Building building = construction.getBuilding();
        building.setLevel(building.getLevel() + 1);
        // any other updates to the building...
buildingRepository.save(building);

        // Remove the construction item from the repository
        constructionRepository.delete(construction);
    }

    public Building findById(Long buildingId) {
        return buildingRepository.findById(buildingId).orElse(null);
    }



}
