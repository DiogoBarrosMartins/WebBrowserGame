package com.superapi.gamerealm.service;

import com.superapi.gamerealm.model.Village;
import com.superapi.gamerealm.model.buildings.BuildingType;
import com.superapi.gamerealm.model.resources.Resources;
import com.superapi.gamerealm.model.resources.TypeOfResource;
import com.superapi.gamerealm.repository.BuildingRepository;
import com.superapi.gamerealm.repository.ResourcesRepository;
import com.superapi.gamerealm.repository.VillageRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
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


    @Transactional
    public void updateVillageResources(Village village) {
        synchronized(village) {
            Resources resources = village.getResources().get(0); // Assuming there's always at least one Resources object in the list

            // calculate the time elapsed since the last update
            LocalDateTime now = LocalDateTime.now();
            long minutesElapsed = Duration.between(village.getLastUpdated(), now).toMinutes();

            // Calculate production for each resource type based on the buildings in the village
            double woodProduction = village.getBuildings().stream()
                    .filter(building -> building.getType() == BuildingType.FOREST)
                    .mapToDouble(building -> building.getProductionRate().doubleValue())
                    .sum();
            double totalWoodProduced = woodProduction * minutesElapsed;

            double wheatProduction = village.getBuildings().stream()
                    .filter(building -> building.getType() == BuildingType.FARM)
                    .mapToDouble(building -> building.getProductionRate().doubleValue())
                    .sum();
            double totalWheatProduced = wheatProduction * minutesElapsed;

            double stoneProduction = village.getBuildings().stream()
                    .filter(building -> building.getType() == BuildingType.QUARRY)
                    .mapToDouble(building -> building.getProductionRate().doubleValue())
                    .sum();
            double totalStoneProduced = stoneProduction * minutesElapsed;

            double goldProduction = village.getBuildings().stream()
                    .filter(building -> building.getType() == BuildingType.MINE)
                    .mapToDouble(building -> building.getProductionRate().doubleValue())
                    .sum();
            double totalGoldProduced = goldProduction * minutesElapsed;

            // Update the resources
            resources.setWood(resources.getWood() + totalWoodProduced);
            resources.setWheat(resources.getWheat() + totalWheatProduced);
            resources.setStone(resources.getStone() + totalStoneProduced);
            resources.setGold(resources.getGold() + totalGoldProduced);


            // Save the updated resources to the database
            resourcesRepository.save(resources);
            // update the lastUpdated timestamp
            village.setLastUpdated(LocalDateTime.now());

            // save the updated village in the database
            villageRepository.save(village);
        }
    }

    public boolean hasEnoughResources(Long villageId, Map<TypeOfResource, Double> resourcesNeeded) {
        Village village = getVillageById(villageId);
        // we will need to return here once we change the resources object
        Resources resources = village.getResources().get(0);

        return resources.getWood() >= resourcesNeeded.getOrDefault(TypeOfResource.WOOD, 0.0) &&
                resources.getWheat() >= resourcesNeeded.getOrDefault(TypeOfResource.WHEAT, 0.0) &&
                resources.getStone() >= resourcesNeeded.getOrDefault(TypeOfResource.STONE, 0.0) &&
                resources.getGold() >= resourcesNeeded.getOrDefault(TypeOfResource.GOLD, 0.0);
    }

    public void deductResources(Long villageId, Map<TypeOfResource, Double> resourcesToDeduct) {
        Village village = getVillageById(villageId);
        //same here
        Resources resources = village.getResources().get(0);
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
