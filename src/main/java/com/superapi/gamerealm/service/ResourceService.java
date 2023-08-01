package com.superapi.gamerealm.service;

import com.superapi.gamerealm.model.Village;
import com.superapi.gamerealm.model.buildings.Building;
import com.superapi.gamerealm.model.buildings.BuildingType;
import com.superapi.gamerealm.model.resources.Resources;
import com.superapi.gamerealm.model.resources.TypeOfResource;
import com.superapi.gamerealm.repository.BuildingRepository;
import com.superapi.gamerealm.repository.ResourcesRepository;
import com.superapi.gamerealm.repository.VillageRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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

// FUBAR
        public void updateResourcesAndLastUpdated(Village village) {
            Resources resources = village.getResources();
            Date lastUpdated = village.getLastUpdated();
            Date currentTime = new Date();
            long secondsElapsed = calculateElapsedSeconds(lastUpdated, currentTime);

            Map<TypeOfResource, BigDecimal> productionPerSecond = calculateProductionPerSecond(village);

            for (BuildingType buildingType : BuildingType.values()) {

                System.out.println("Processing building type: " + buildingType);
                TypeOfResource resourceType = buildingType.getResourceName();
                BigDecimal produced = productionPerSecond.getOrDefault(buildingType, BigDecimal.ZERO).multiply(BigDecimal.valueOf(secondsElapsed));
                resources.setAmount(resourceType, resources.getAmount(resourceType).add(produced).setScale(0, RoundingMode.DOWN));
            }

        // Save the updated resources
        resourcesRepository.save(resources);

        village.setLastUpdated(currentTime);
        village.setResources(resources);

        // Save the updated village with the updated resources
        villageRepository.save(village);
    }
    private long calculateElapsedSeconds(Date lastUpdateTime, Date currentTime) {
        long timeElapsedInMillis = currentTime.getTime() - lastUpdateTime.getTime();
        return timeElapsedInMillis / 1000; // Convert milliseconds to seconds
    }

    public Map<TypeOfResource, BigDecimal> calculateProductionPerSecond(Village village) {
        Map<TypeOfResource, BigDecimal> productionPerSecond = new HashMap<>();

        for (Building building : village.getBuildings()) {
            if (building.isResourceBuilding()) {
                BigDecimal productionRatePerHour = building.calculateProductionRate();
                BigDecimal productionRatePerSecond = productionRatePerHour.divide(BigDecimal.valueOf(3600), 2, RoundingMode.DOWN);
                TypeOfResource resourceType = building.getType().getResourceName();
                productionPerSecond.put(resourceType, productionRatePerSecond);
            }
        }

        return productionPerSecond;
    }

    public boolean hasEnoughResources(Long villageId, int[] resourcesNeeded) {
        Village village = getVillageById(villageId);
        Resources resources = village.getResources();

        return resources.getAmount(TypeOfResource.WHEAT).compareTo(BigDecimal.valueOf(resourcesNeeded[0])) >= 0 &&
                resources.getAmount(TypeOfResource.GOLD).compareTo(BigDecimal.valueOf(resourcesNeeded[1])) >= 0 &&
                resources.getAmount(TypeOfResource.WOOD).compareTo(BigDecimal.valueOf(resourcesNeeded[2])) >= 0 &&
                resources.getAmount(TypeOfResource.STONE).compareTo(BigDecimal.valueOf(resourcesNeeded[3])) >= 0;
    }

    public Resources getResourceInventory(Long villageId) {
        Village village = getVillageById(villageId);
        return village.getResources();
    }

    public void deductResources(Building building , int[] resourcesNeeded) {
Resources resources = building.getVillage().getResources();
        resources.setAmount(TypeOfResource.WHEAT, resources.getAmount(TypeOfResource.WHEAT).subtract(BigDecimal.valueOf(resourcesNeeded[building.getLevel()+1])));
        resources.setAmount(TypeOfResource.GOLD, resources.getAmount(TypeOfResource.GOLD).subtract(BigDecimal.valueOf(resourcesNeeded[building.getLevel()+1])));
        resources.setAmount(TypeOfResource.STONE, resources.getAmount(TypeOfResource.STONE).subtract(BigDecimal.valueOf(resourcesNeeded[building.getLevel()+1])));
        resources.setAmount(TypeOfResource.WOOD, resources.getAmount(TypeOfResource.WOOD).subtract(BigDecimal.valueOf(resourcesNeeded[building.getLevel()+1])));
        resourcesRepository.save(resources);
    }

    private Village getVillageById(Long villageId) {
        return villageRepository.findById(villageId).orElseThrow(RuntimeException::new);
    }



}
