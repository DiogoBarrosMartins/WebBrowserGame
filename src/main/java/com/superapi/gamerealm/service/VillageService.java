package com.superapi.gamerealm.service;

import com.superapi.gamerealm.component.Coordinates;
import com.superapi.gamerealm.dto.VillageDTO;
import com.superapi.gamerealm.dto.VillageMapper;
import com.superapi.gamerealm.model.Account;
import com.superapi.gamerealm.model.Grid;
import com.superapi.gamerealm.model.Village;
import com.superapi.gamerealm.model.resources.Resources;
import com.superapi.gamerealm.repository.VillageRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        Coordinates coordinates = getCoordinatesAt(grid, x, y);
        return coordinates != null && !coordinates.hasVillage();
    }

    public List<Coordinates> getAllAvailableSpots(Grid grid) {
        List<Coordinates> availableSpots = new ArrayList<>();

        for (int x = 0; x < grid.getWidth(); x++) {
            for (int y = 0; y < grid.getHeight(); y++) {
                Coordinates coordinates = getCoordinatesAt(grid, x, y);
                if (coordinates != null && !coordinates.hasVillage()) {
                    availableSpots.add(coordinates);
                }
            }
        }

        return availableSpots;
    }

    public Village createVillageForAccount(Account account) {
        Grid grid = gridService.getGrid();
        List<Coordinates> availableSpots = getAllAvailableSpots(grid);

        if (availableSpots.isEmpty()) {
            throw new RuntimeException("No available spot for village.");
        }

        // Select a random available spot from the list
        int randomIndex = (int) (Math.random() * availableSpots.size());
        Coordinates spot = availableSpots.get(randomIndex);

        spot.setHasVillage(true); // Update the grid to indicate that a village has been created at the selected coordinate

        // Create the new Village entity with the selected coordinates
        Village newVillage = new Village(spot);
        newVillage.setAccount(account);
        initializeDefaultResources(newVillage);
        villageRepository.save(newVillage);


        // Remove the selected spot from the list to avoid duplicates
        availableSpots.remove(randomIndex);

        return newVillage;
    }

    private void initializeDefaultResources(Village village) {
        // Create a new Resources instance
        Resources resources = new Resources();

        // Set default resource amounts
        resources.setWheat(BigDecimal.valueOf(1000L));
        resources.setWood(BigDecimal.valueOf(1000L));
        resources.setStone(BigDecimal.valueOf(500L));
        resources.setGold(BigDecimal.valueOf(500L));

        // Assign the resources to the village
        village.setResources(resources);
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


    // Method to get all villages as DTOs
    public List<VillageDTO> getAllVillages() {
        List<Village> villages = villageRepository.findAll();
        return villages.stream()
                .map(VillageMapper::toDTO) // Use VillageMapper.toDTO here
                .collect(Collectors.toList());
    }

    // Method to get village by account username as DTO
    public VillageDTO getVillageByAccountUsername(String username) {
        Village village = villageRepository.findByAccountUsername(username)
                .orElseThrow(() -> new RuntimeException("Village not found for account username: " + username));
        return convertToVillageDTO(village);
    }

    private VillageDTO convertToVillageDTO(Village village) {
        return modelMapper.map(village, VillageDTO.class);
    }




    /**



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