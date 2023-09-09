package com.superapi.gamerealm.service;

import com.superapi.gamerealm.dto.building.BuildingMapper;
import com.superapi.gamerealm.dto.building.NonResourceBuildingDTO;
import com.superapi.gamerealm.dto.building.ResourceBuildingDTO;
import com.superapi.gamerealm.model.buildings.Building;
import com.superapi.gamerealm.model.buildings.BuildingType;
import com.superapi.gamerealm.model.buildings.Construction;
import com.superapi.gamerealm.model.resources.TypeOfResource;
import com.superapi.gamerealm.model.resources.Upgrade;
import com.superapi.gamerealm.model.troop.TroopType;
import com.superapi.gamerealm.repository.BuildingRepository;
import com.superapi.gamerealm.repository.ConstructionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
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

    private final ConstructionRepository constructionRepository;

    @Autowired
    public BuildingService(BuildingRepository buildingRepository, ConstructionRepository constructionRepository, ResourceService resourceService) {
        this.buildingRepository = buildingRepository;
        this.constructionRepository = constructionRepository;
        this.resourceService = resourceService;

    }

    public List<ResourceBuildingDTO> getAllResourceBuildingsInVillage(Long villageId) {
        List<Building> buildings = buildingRepository.findByVillageId(villageId);

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
            construction.setBuildingId(building.getId());
            construction.setVillage(building.getVillage());

            // Calculate the upgrade time and set the construction's startedAt and endsAt
            Double upgradeTimeInMinutes = building.isResourceBuilding() ?
                    Upgrade.RESOURCE_BUILDING_UPGRADE_TIMES[building.getLevel()] :
                    Upgrade.NON_RESOURCE_BUILDING_UPGRADE_TIMES[building.getLevel()];


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


    public void processOverdueConstructions(Long villageId) {
        List<Construction> constructions = constructionRepository.findByVillageId(villageId);
        LocalDateTime now = LocalDateTime.now();
        for (Construction construction : constructions) {
            if (construction.getEndsAt().isBefore(now)) {
                completeConstruction(construction);
            }
        }
    }

    private void scheduleConstructionCompletion(Construction construction) {
        long delay = Duration.between(LocalDateTime.now(), construction.getEndsAt()).toMillis();
        System.out.println("Scheduling construction completion with a delay of: " + delay + " milliseconds.");

        CompletableFuture.delayedExecutor(delay, TimeUnit.MILLISECONDS)
                .execute(() -> completeConstruction(construction));
    }

    public void completeConstruction(Construction construction) {
        System.out.println("Starting construction completion check for construction ID: " + construction.getId());

        if (construction.getEndsAt().isBefore(LocalDateTime.now())) {
            System.out.println("Construction with ID " + construction.getId() + " is ready for completion.");

            Building building = buildingRepository.findById(construction.getBuildingId()).orElseThrow();

            building.setLevel(building.getLevel() + 1);
            building.setProductionRate(BigDecimal.valueOf(Upgrade.RESOURCE_BUILDING_PRODUCTION_RATES[building.getLevel()]));
            buildingRepository.save(building);

            System.out.println("Building with ID " + building.getId() + " has been upgraded to level " + building.getLevel());

            // Remove construction from the queue
            construction.getVillage().getConstructions().remove(construction);
            constructionRepository.delete(construction);

            System.out.println("Construction with ID " + construction.getId() + " has been removed from the queue.");
        } else {
            System.out.println("Construction with ID " + construction.getId() + " is not yet ready for completion.");
        }
    }


    public Building findById(Long buildingId) {
        return buildingRepository.findById(buildingId).orElse(null);
    }


    public List<String> getBuildableTroopsForBuilding(Building building) {
        List<String> buildableTroops = new ArrayList<>();

        // Check race
        String race = building.getVillage().getAccount().getTribe();

        // Check building level
        int level = building.getLevel();

        // Determine the appropriate building type for the given troop type
        BuildingType buildingType;
        switch (race) {
            case "human":
                switch (level) {
                    case 1:
                        buildingType = BuildingType.BARRACKS;
                        break;
                    case 2:
                        buildingType = BuildingType.ARCHERY_RANGE;
                        break;
                    case 3:
                        buildingType = BuildingType.STABLE;
                        break;
                    case 4:
                        buildingType = BuildingType.SIEGE_WORKSHOP;
                        break;
                    default:
                        // Unsupported building level
                        return buildableTroops;
                }
                break;
            case "orc":
                switch (level) {
                    case 1:
                        buildingType = BuildingType.BARRACKS;
                        break;
                    // Add logic for orc building levels 2, 3, and 4 here
                    // ...
                    default:
                        // Unsupported building level
                        return buildableTroops;
                }
                break;
            case "elf":
                switch (level) {
                    case 1:
                        buildingType = BuildingType.BARRACKS;
                        break;
                    // Add logic for elf building levels 2, 3, and 4 here
                    // ...
                    default:
                        // Unsupported building level
                        return buildableTroops;
                }
                break;
            default:
                // Unsupported player race
                return buildableTroops;
        }

        // Filter troops based on their names and descriptions
        for (TroopType troopType : TroopType.values()) {
            String troopName = troopType.name();
            String troopDescription = troopType.getDescription();

            // Check if the troop's name starts with "ORC" and the description contains "foot troop"
            if (troopName.startsWith("ORC") && troopDescription.contains("foot troop")) {
                if (buildingType == BuildingType.BARRACKS) {
                    buildableTroops.add(troopName);
                }
            }


        }

        return buildableTroops;
    }

}
