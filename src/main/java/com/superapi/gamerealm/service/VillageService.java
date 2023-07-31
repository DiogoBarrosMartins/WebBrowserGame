package com.superapi.gamerealm.service;

import com.superapi.gamerealm.component.Coordinates;
import com.superapi.gamerealm.dto.VillageDTO;
import com.superapi.gamerealm.model.Account;
import com.superapi.gamerealm.model.Grid;
import com.superapi.gamerealm.model.Village;
import com.superapi.gamerealm.repository.VillageRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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
    public boolean isSpotAvailable(Grid grid, int x, int y) {
        // Check if the spot at coordinates (x, y) is available (i.e., no village is already there)
        return grid.getVillageCoordinates().stream()
                .noneMatch(coordinates -> coordinates.getX() == x && coordinates.getY() == y && coordinates.hasVillage());
    }




    //here we is

    public Village createVillageForAccount(Account account) {
        Grid grid = gridService.getGrid(); // You can use the method from before to get the grid
        System.out.println("WE TRIED TO CREATE A VILLAGE XDDDD");
        // Find an available spot on the grid around the center (0,0)
        int x = (grid.getHeight()/2);
        int y = (grid.getWidth()/2);

        Coordinates villageCoordinates = null; // Initialize a variable to hold the village coordinates
        // this goes out of bounds because we are in a 5 by 5 grid
        for (int distance = 1; distance <= 2; distance++) {
            // Check the four cardinal directions (up, down, left, right) around the center
            if (isSpotAvailable(grid, x + distance, y)) {
                x += distance;
                villageCoordinates = getCoordinatesAt(grid, x, y);
                break;
            } else if (isSpotAvailable(grid, x - distance, y)) {
                x -= distance;
                villageCoordinates = getCoordinatesAt(grid, x, y);
                break;
            } else if (isSpotAvailable(grid, x, y + distance)) {
                y += distance;
                villageCoordinates = getCoordinatesAt(grid, x, y);
                break;
            } else if (isSpotAvailable(grid, x, y - distance)) {
                y -= distance;
                villageCoordinates = getCoordinatesAt(grid, x, y);
                break;
            }
        }

        Village newVillage = new Village(x, y);
        newVillage.setAccount(account);

        // Update the Grid entity with the new village coordinates
         villageCoordinates = getCoordinatesAt(grid, x, y);
        if (villageCoordinates != null) {
            villageCoordinates.setHasVillage(true);
        } else {
            throw new RuntimeException("Invalid coordinates for village.");
        }

        villageRepository.save(newVillage);        return newVillage;
}

    private Coordinates getCoordinatesAt(Grid grid, int x, int y) {
        return grid.getVillageCoordinates().stream()
                .filter(coordinates -> coordinates.getX() == x && coordinates.getY() == y)
                .findFirst()
                .orElse(null);
    }




    public VillageDTO createVillage(VillageDTO villageDTO) {
        Village village = modelMapper.map(villageDTO, Village.class);
        Village createdVillage = villageRepository.save(village);
        return modelMapper.map(createdVillage, VillageDTO.class);
    }

    public Village getVillageByCoordinates(int x, int y) {
        return villageRepository.findByCoordinatesXAndCoordinatesY(x, y);
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