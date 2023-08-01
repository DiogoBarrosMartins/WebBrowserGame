package com.superapi.gamerealm.service;

import com.superapi.gamerealm.model.Village;
import com.superapi.gamerealm.model.buildings.Building;
import com.superapi.gamerealm.model.resources.Resources;
import com.superapi.gamerealm.repository.BuildingRepository;
import com.superapi.gamerealm.repository.ResourcesRepository;
import com.superapi.gamerealm.repository.VillageRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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

    public Map<String, BigDecimal> calculateProductionPerHour(Village village) {
        Map<String, BigDecimal> productionPerHour = new HashMap<>();

        for (Building building : village.getBuildings()) {
            if (building.isResourceBuilding()) {

                BigDecimal productionRatePerHour = building.calculateProductionRate();
                productionPerHour.put(building.getType().name(), productionRatePerHour);
            }
        }

        return productionPerHour;
    }

     public void updateResourcesAndLastUpdated(Village village) {
        Resources resources = village.getResources();
        Date lastUpdated = village.getLastUpdated();
        Date currentTime = new Date();
        long hoursElapsed = calculateElapsedHours(lastUpdated, currentTime);

        Map<String, BigDecimal> productionPerHour = calculateProductionPerHour(village);

        BigDecimal wheatProduced = productionPerHour.getOrDefault("FARM", BigDecimal.ZERO).multiply(BigDecimal.valueOf(hoursElapsed));
        resources.setWheat(resources.getWheat().add(wheatProduced));

        // Update other resources (e.g., gold, wood, stone) similarly based on their respective production rates
        // For demonstration purposes, I'll assume the keys in productionPerHour are "GOLD", "WOOD", and "STONE"

        BigDecimal goldProduced = productionPerHour.getOrDefault("GOLD", BigDecimal.ZERO).multiply(BigDecimal.valueOf(hoursElapsed));
        resources.setGold(resources.getGold().add(goldProduced));

        BigDecimal woodProduced = productionPerHour.getOrDefault("WOOD", BigDecimal.ZERO).multiply(BigDecimal.valueOf(hoursElapsed));
        resources.setWood(resources.getWood().add(woodProduced));

        BigDecimal stoneProduced = productionPerHour.getOrDefault("STONE", BigDecimal.ZERO).multiply(BigDecimal.valueOf(hoursElapsed));
        resources.setStone(resources.getStone().add(stoneProduced));

        resourcesRepository.save(resources);

        village.setLastUpdated(currentTime);
        villageRepository.save(village);
    }

    private long calculateElapsedHours(Date lastUpdateTime, Date currentTime) {
        long timeElapsedInMillis = currentTime.getTime() - lastUpdateTime.getTime();
        return timeElapsedInMillis / (1000 * 60 * 60); // Convert milliseconds to hours
    }
}
