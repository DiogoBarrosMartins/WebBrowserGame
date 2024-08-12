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
        // Debugging output for the building and resources
        System.out.println("Starting deductResourcesAndInitiateConstruction");
        System.out.println("Building ID: " + building.getId());
        System.out.println("Village ID: " + building.getVillage().getId());
        System.out.println("Resources needed: " + resourcesNeeded);

        resourceService.deductResources(building.getVillage().getId(), resourcesNeeded);
        System.out.println("Resources deducted for village ID: " + building.getVillage().getId());

        // Debugging output for construction
        Construction construction = new Construction();
        construction.setBuildingId(building.getId());
        construction.setVillage(building.getVillage());
        LocalDateTime now = LocalDateTime.now();
        construction.setStartedAt(now);
        System.out.println("Construction started at: " + now);

        LocalDateTime endsAt = building.getTimeToUpgrade();
        if (endsAt == null) {
            System.out.println("Warning: building.getTimeToUpgrade() is null.");
        } else {
            System.out.println("Building time to upgrade ends at: " + endsAt);
        }
        construction.setEndsAt(endsAt);

        System.out.println("Saving construction for building ID: " + building.getId());
        constructionRepository.save(construction);

        building.setTimeToUpgrade(endsAt);
        System.out.println("Updated building's time to upgrade: " + endsAt);
        buildingRepository.save(building);

        System.out.println("Scheduling construction completion");
        scheduleConstructionCompletion(construction);

        System.out.println("Finished deductResourcesAndInitiateConstruction for building ID: " + building.getId());
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
