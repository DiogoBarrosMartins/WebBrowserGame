package com.superapi.gamerealm.service;

import com.superapi.gamerealm.model.Village;
import com.superapi.gamerealm.model.buildings.Building;
import com.superapi.gamerealm.model.resources.Resources;
import com.superapi.gamerealm.model.resources.TypeOfResource;
import com.superapi.gamerealm.repository.BuildingRepository;
import com.superapi.gamerealm.repository.ResourcesRepository;
import com.superapi.gamerealm.repository.VillageRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

@Service
public class ResourceService {
    private final BuildingRepository buildingRepository;
    private final ResourcesRepository resourcesRepository;
    private final VillageRepository villageRepository;

    public ResourceService(BuildingRepository buildingRepository, ResourcesRepository resourcesRepository, VillageRepository villageRepository) {
        this.buildingRepository = buildingRepository;
        this.resourcesRepository = resourcesRepository;
        this.villageRepository = villageRepository;
    }
    public BigDecimal calculateProductionRate(Building building) {
        // Define a base production rate for level 0
        BigDecimal baseProductionRate = building.getProductionRate();

        // Define a rate of increase per level
        BigDecimal increasePerLevel = new BigDecimal("5");

        // Calculate the production rate based on the building's level
        // Return the calculated production rate
        return baseProductionRate.add(increasePerLevel.multiply(new BigDecimal(building.getLevel())));
    }


    public void updateResourcesAndLastUpdated(Village village) {
        List<Resources> resourcesList = village.getResources();
        Date lastUpdated = village.getLastUpdated();
        Date currentTime = new Date();
        long secondsElapsed = calculateElapsedSeconds(lastUpdated, currentTime);

        for (Building building : village.getBuildings()) {
            if (building.isResourceBuilding()) {
                TypeOfResource resourceType = building.getType().getResourceName();
                BigDecimal productionRatePerHour = calculateProductionRate(building);
                BigDecimal productionRatePerSecond = productionRatePerHour.divide(BigDecimal.valueOf(3600), 2, RoundingMode.HALF_UP);
                BigDecimal produced = productionRatePerSecond.multiply(BigDecimal.valueOf(secondsElapsed));

                // Find the corresponding Resources object for the resource type
                Resources resources = resourcesList.stream()
                        .filter(r -> r.getType() == resourceType)
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("No Resources object found for resource type " + resourceType));

                // Update the amount of the corresponding Resources object
                resources.setAmount(resources.getAmount().add(produced).setScale(0, RoundingMode.DOWN));
            }
        }

        // Save the updated resources
        resourcesRepository.saveAll(resourcesList);

        village.setLastUpdated(currentTime);

        // Save the updated village with the updated resources
        villageRepository.save(village);
    }

    private long calculateElapsedSeconds(Date lastUpdateTime, Date currentTime) {
        long timeElapsedInMillis = currentTime.getTime() - lastUpdateTime.getTime();
        return timeElapsedInMillis / 1000; // Convert milliseconds to seconds
    }

    public boolean hasEnoughResources(Long villageId, int[] resourcesNeeded) {
        Village village = getVillageById(villageId);
        List<Resources> resourcesList = village.getResources();

        return resourcesList.stream()
                .filter(r -> r.getType() == TypeOfResource.WHEAT || r.getType() == TypeOfResource.GOLD ||
                        r.getType() == TypeOfResource.WOOD || r.getType() == TypeOfResource.STONE)
                .allMatch(r -> r.getAmount().compareTo(BigDecimal.valueOf(resourcesNeeded[r.getType().ordinal()])) >= 0);
    }

    public void deductResources(Building building, int[] resourcesNeeded) {
        Village village = building.getVillage();
        List<Resources> resourcesList = village.getResources();

        for (Resources resources : resourcesList) {
            TypeOfResource resourceType = resources.getType();
            BigDecimal amountToDeduct = BigDecimal.valueOf(resourcesNeeded[resourceType.ordinal()]);

            if (resources.getAmount().compareTo(amountToDeduct) >= 0) {
                resources.setAmount(resources.getAmount().subtract(amountToDeduct));
            } else {
                throw new RuntimeException("Not enough " + resourceType + " to deduct for building " + building.getType());
            }
        }

        // Save the updated resources
        resourcesRepository.saveAll(resourcesList);
    }

    private Village getVillageById(Long villageId) {
        return villageRepository.findById(villageId).orElseThrow(RuntimeException::new);
    }

    public int getTotalResources(Resources defenderResources) {
        // this is not an int
        //todo
        return 420;
    }

    public Resources createResources(int lootAmount) {
        //todo xdd
        return new Resources(TypeOfResource.WHEAT, BigDecimal.valueOf(420));
    }

    public Resources createResource(Resources gold, int food, int wood, Resources stone) {
        return new Resources();
    }
}
