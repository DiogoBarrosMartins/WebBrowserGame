package com.superapi.gamerealm.service;

import com.superapi.gamerealm.model.Village;
import com.superapi.gamerealm.model.buildings.Building;
import com.superapi.gamerealm.model.buildings.BuildingType;
import com.superapi.gamerealm.model.resources.Resources;
import com.superapi.gamerealm.model.resources.TypeOfResource;
import com.superapi.gamerealm.model.resources.Upgrade;
import com.superapi.gamerealm.repository.BuildingRepository;
import com.superapi.gamerealm.repository.ResourcesRepository;
import com.superapi.gamerealm.repository.VillageRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
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
    public BigDecimal calculateProductionRate(Building building) {
        // Define a base production rate for level 0
        BigDecimal baseProductionRate = building.getProductionRate();

        // Define a rate of increase per level
        BigDecimal increasePerLevel = new BigDecimal("5");

        // Calculate the production rate based on the building's level
        // Return the calculated production rate
        return baseProductionRate.add(increasePerLevel.multiply(new BigDecimal(building.getLevel())));
    }



    // Assume there's a maximum limit for each resource
    private static final double MAX_RESOURCE = 5000.0;

    public void updateVillageResources(Village village) {
        synchronized(village) {
            Resources resources = village.getResources().get(0); // Assuming there's always at least one Resources object in the list
            List<Building> buildings = village.getBuildings();

            // calculate the time elapsed since the last update
            Duration timeElapsed = Duration.between(village.getLastUpdated(), LocalDateTime.now());

            // calculate total production rates for each type of resource
            double totalWoodProductionRate = buildings.stream()
                    .filter(b -> b.getType().equals(BuildingType.FOREST))
                    .mapToDouble(b -> b.getProductionRate().doubleValue())
                    .sum();

            double totalWheatProductionRate = buildings.stream()
                    .filter(b -> b.getType().equals(BuildingType.FARM ))
                    .mapToDouble(b -> b.getProductionRate().doubleValue())
                    .sum();

            double totalStoneProductionRate = buildings.stream()
                    .filter(b -> b.getType().equals(BuildingType.QUARRY))
                    .mapToDouble(b -> b.getProductionRate().doubleValue())
                    .sum();

            double totalGoldProductionRate = buildings.stream()
                    .filter(b -> b.getType().equals(BuildingType.MINE))
                    .mapToDouble(b -> b.getProductionRate().doubleValue())
                    .sum();

            // update each resource
            double newWood = Math.min(resources.getWood() + timeElapsed.toHours() * totalWoodProductionRate, MAX_RESOURCE);
            resources.setWood(newWood);

            double newWheat = Math.min(resources.getWheat() + timeElapsed.toHours() * totalWheatProductionRate, MAX_RESOURCE);
            resources.setWheat(newWheat);

            double newStone = Math.min(resources.getStone() + timeElapsed.toHours() * totalStoneProductionRate, MAX_RESOURCE);
            resources.setStone(newStone);

            double newGold = Math.min(resources.getGold() + timeElapsed.toHours() * totalGoldProductionRate, MAX_RESOURCE);
            resources.setGold(newGold);

            // update the lastUpdated timestamp
            village.setLastUpdated(LocalDateTime.now());

            // save the updated village in the database
            villageRepository.save(village);
        }
    }

    public boolean hasEnoughResources(Long villageId, Map<TypeOfResource, Double> resourcesNeeded) {
        Village village = getVillageById(villageId);
        Resources resources = village.getResources().get(0); // Assuming there's always at least one Resources object in the list

        return resources.getWood() >= resourcesNeeded.getOrDefault(TypeOfResource.WOOD, 0.0) &&
                resources.getWheat() >= resourcesNeeded.getOrDefault(TypeOfResource.WHEAT, 0.0) &&
                resources.getStone() >= resourcesNeeded.getOrDefault(TypeOfResource.STONE, 0.0) &&
                resources.getGold() >= resourcesNeeded.getOrDefault(TypeOfResource.GOLD, 0.0);
    }

    public void deductResources(Long villageId, Map<TypeOfResource, Double> resourcesToDeduct) {
        Village village = getVillageById(villageId);
        Resources resources = village.getResources().get(0); // Assuming there's always at least one Resources object in the list

        double newWood = resources.getWood() - resourcesToDeduct.getOrDefault(TypeOfResource.WOOD, 0.0);
        if (newWood >= 0) {
            resources.setWood(newWood);
        } else {
            throw new RuntimeException("Not enough wood to deduct");
        }

        double newWheat = resources.getWheat() - resourcesToDeduct.getOrDefault(TypeOfResource.WHEAT, 0.0);
        if (newWheat >= 0) {
            resources.setWheat(newWheat);
        } else {
            throw new RuntimeException("Not enough wheat to deduct");
        }

        double newStone = resources.getStone() - resourcesToDeduct.getOrDefault(TypeOfResource.STONE, 0.0);
        if (newStone >= 0) {
            resources.setStone(newStone);
        } else {
            throw new RuntimeException("Not enough stone to deduct");
        }

        double newGold = resources.getGold() - resourcesToDeduct.getOrDefault(TypeOfResource.GOLD, 0.0);
        if (newGold >= 0) {
            resources.setGold(newGold);
        } else {
            throw new RuntimeException("Not enough gold to deduct");
        }

        // Save the updated resources
        resourcesRepository.save(resources);
    }


    private Village getVillageById(Long villageId) {
        return villageRepository.findById(villageId).orElseThrow(RuntimeException::new);
    }


}
