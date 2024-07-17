package com.superapi.gamerealm.service;

import com.superapi.gamerealm.dto.building.BuildingMapper;
import com.superapi.gamerealm.dto.building.NonResourceBuildingDTO;
import com.superapi.gamerealm.dto.building.ResourceBuildingDTO;
import com.superapi.gamerealm.model.buildings.Building;

import com.superapi.gamerealm.model.buildings.Construction;
import com.superapi.gamerealm.model.resources.TypeOfResource;
import com.superapi.gamerealm.model.resources.Upgrade;
import com.superapi.gamerealm.repository.BuildingRepository;
import com.superapi.gamerealm.repository.ConstructionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class BuildingService {

    private final BuildingRepository buildingRepository;
    private final ConstructionRepository constructionRepository;
    private final ResourceService resourceService;

    @Autowired
    public BuildingService(BuildingRepository buildingRepository, ConstructionRepository constructionRepository, ResourceService resourceService) {
        this.buildingRepository = buildingRepository;
        this.constructionRepository = constructionRepository;
        this.resourceService = resourceService;
    }

    public List<ResourceBuildingDTO> getAllResourceBuildingsInVillage(Long villageId) {
        List<Building> buildings = buildingRepository.findByVillageId(villageId);
        return buildings.stream()
                .filter(Building::isResourceBuilding)
                .map(BuildingMapper::toResourceBuildingDTO)
                .collect(Collectors.toList());
    }

    public List<NonResourceBuildingDTO> getAllNonResourceBuildingsInVillage(Long villageId) {
        List<Building> buildings = buildingRepository.findByVillageId(villageId);
        return buildings.stream()
                .filter(building -> !building.isResourceBuilding())
                .map(BuildingMapper::toNonResourceBuildingDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public Building upgradeBuilding(Long buildingId) {
        Building building = buildingRepository.findById(buildingId)
                .orElseThrow(() -> new IllegalArgumentException("Building not found"));

        validateUpgradeConditions(building);

        Map<TypeOfResource, Double> resourcesNeeded = Upgrade.getResourceNeeded(building.getType(), building.getBuildingLevel());
        validateResourcesForUpgrade(building.getVillage().getId(), resourcesNeeded);

        deductResourcesAndInitiateConstruction(building, resourcesNeeded);

        return building;
    }

    @Transactional
    public void processOverdueConstructions(Long villageId) {
        List<Construction> constructions = constructionRepository.findByVillageId(villageId);
        LocalDateTime now = LocalDateTime.now();
        constructions.stream()
                .filter(construction -> construction.getEndsAt().isBefore(now))
                .forEach(this::completeConstruction);
    }

    private void validateUpgradeConditions(Building building) {
        if (constructionRepository.findByBuildingId(building.getId()) != null) {
            throw new IllegalStateException("An upgrade is already in progress for this building.");
        }

        if (building.getBuildingLevel() >= building.getMaxLevel()) {
            throw new IllegalStateException("Building is already at the maximum level.");
        }
    }

    private void validateResourcesForUpgrade(Long villageId, Map<TypeOfResource, Double> resourcesNeeded) {
        if (!resourceService.hasEnoughResources(villageId, resourcesNeeded)) {
            throw new IllegalStateException("Player doesn't have enough resources to upgrade this building.");
        }
    }
    private void deductResourcesAndInitiateConstruction(Building building, Map<TypeOfResource, Double> resourcesNeeded) {
        resourceService.deductResources(building.getVillage().getId(), resourcesNeeded);


        // Create a new Construction instance
        Construction construction = new Construction();
        construction.setBuildingId(building.getId());
        construction.setVillage(building.getVillage());
        LocalDateTime now = LocalDateTime.now();
        construction.setStartedAt(now);

        // Calculate and set the end time for construction
        LocalDateTime endsAt = now.plusMinutes(building.getTimeToUpgrade().getMinute()); // Assuming timeToUpgrade is a LocalDateTime
        construction.setEndsAt(endsAt);

        // Save the construction instance
        constructionRepository.save(construction);

        // Set timeToUpgrade in the Building instance
        building.setTimeToUpgrade(construction.getEndsAt());
        buildingRepository.save(building); // Save the updated Building instance

        // Schedule construction completion
        scheduleConstructionCompletion(construction);
    }


    private void scheduleConstructionCompletion(Construction construction) {
        long delay = Duration.between(LocalDateTime.now(), construction.getEndsAt()).toMillis();
        CompletableFuture.delayedExecutor(delay, TimeUnit.MILLISECONDS)
                .execute(() -> completeConstruction(construction));
    }

    @Transactional
    public void completeConstruction(Construction construction) {
        if (construction.getEndsAt().isBefore(LocalDateTime.now())) {
            Building building = buildingRepository.findById(construction.getBuildingId())
                    .orElseThrow(() -> new IllegalStateException("Building not found"));

            building.setBuildingLevel(building.getBuildingLevel() + 1);
            building.setProductionRate(BigDecimal.valueOf(Upgrade.getProductionRate(building.getType(), building.getBuildingLevel())));
            buildingRepository.save(building);

            construction.getVillage().getConstructions().remove(construction);
            constructionRepository.delete(construction);
        }
    }

    public Building findById(Long buildingId) {
        return buildingRepository.findById(buildingId).orElse(null);
    }
}
