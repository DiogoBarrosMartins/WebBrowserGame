package com.superapi.gamerealm.service;

import com.superapi.gamerealm.dto.VillageDTO;
import com.superapi.gamerealm.model.Account;
import com.superapi.gamerealm.model.Grid;
import com.superapi.gamerealm.model.Village;
import com.superapi.gamerealm.repository.VillageRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class VillageService {
    private final VillageRepository villageRepository;
    private final ModelMapper modelMapper;
    private final GridService gridService;

    @Autowired
    public VillageService(VillageRepository villageRepository, GridService gridService, ModelMapper modelMapper) {
        this.villageRepository = villageRepository;
        this.gridService = gridService;
        this.modelMapper = modelMapper;

    }

    public VillageDTO createVillage(VillageDTO villageDTO) {
        Village village = modelMapper.map(villageDTO, Village.class);
        Village createdVillage = villageRepository.save(village);
        return modelMapper.map(createdVillage, VillageDTO.class);
    }










    /**
    private void initializeDefaultResources(Village village) {
        // Set default resource amounts
        Map<String, Long> resources = new HashMap<>();
        resources.put("wheat", 1000L);
        resources.put("wood", 1000L);
        resources.put("stone", 500L);
        resources.put("gold", 500L);
        village.setResources(resources);
    }




    public long calculateElapsedHours(Date lastUpdateTime, Date now) {
        long elapsedMilliseconds = now.getTime() - lastUpdateTime.getTime();
        return elapsedMilliseconds / (1000 * 3600); // Convert elapsedMilliseconds to elapsedHours
    }


    public Map<String, Long> calculateResourcesProduced(Village village, long elapsedHours) {
        Map<String, Long> resourcesProduced = new HashMap<>();
        Map<String, Integer> buildingLevels = village.getBuildings();

        // Production rates for each resource building
        Map<String, Long> productionRates = new HashMap<>();
        productionRates.put("farms", 1000L);
        productionRates.put("lumbers", 500L);
        productionRates.put("rockMines", 300L);
        productionRates.put("goldMines", 100L);

        // Calculate the resources produced based on building levels and elapsed hours
        for (String resourceBuilding : productionRates.keySet()) {
            int buildingLevel = buildingLevels.getOrDefault(resourceBuilding, 0);
            long productionRate = productionRates.get(resourceBuilding);
            long producedAmount = buildingLevel * productionRate * elapsedHours;
            resourcesProduced.put(resourceBuilding, producedAmount);
        }

        return resourcesProduced;
    }


    public void updateVillageResources(Village village, Map<String, Long> resourcesProduced, Date now) {
        Map<String, Long> currentResources = village.getResources();

        for (String resource : resourcesProduced.keySet()) {
            long producedAmount = resourcesProduced.get(resource);
            long currentAmount = currentResources.getOrDefault(resource, 0L);
            long updatedAmount = currentAmount + producedAmount;
            currentResources.put(resource, updatedAmount);
        }

        village.setResources(currentResources);
        village.setLastUpdated(now);
    }

    public void purgePlayerData() {
    }

    public List<Village> findAllVillagesByAccountId(Long accountId) {
        return villageRepository.findAllByAccountId(accountId);
    }

    public void turnVillageIntoConquerableSpots(Village village) {
        if (village != null) {
            // Get the village's coordinates
            int xCoordinate = village.getXCoordinate();
            int yCoordinate = village.getYCoordinate();

            // Remove the village from the player's account
            villageRepository.delete(village);

            // Call the GridService to add a conquerable spot
        }
    }
    public void addVillageFromConquerableSpot(Grid grid, Account account) {

        }
    }    // Other methods related to village business logic

**/
}