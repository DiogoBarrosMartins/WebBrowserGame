package com.superapi.gamerealm.service;

import com.superapi.gamerealm.component.Coordinates;
import com.superapi.gamerealm.dto.VillageDTO;
import com.superapi.gamerealm.dto.VillageMapper;
import com.superapi.gamerealm.model.Account;
import com.superapi.gamerealm.model.Grid;
import com.superapi.gamerealm.model.Village;
import com.superapi.gamerealm.model.buildings.Building;
import com.superapi.gamerealm.model.buildings.BuildingType;
import com.superapi.gamerealm.model.resources.Resources;
import com.superapi.gamerealm.repository.ResourcesRepository;
import com.superapi.gamerealm.repository.VillageRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class VillageService {
    private final VillageRepository villageRepository;
    private final ModelMapper modelMapper;
    private final GridService gridService;
    private final ResourceService resourceService;
    private final ResourcesRepository resourcesRepository;

    @Autowired
    public VillageService(VillageRepository villageRepository, GridService gridService, ModelMapper modelMapper, ResourceService resourceService, ResourcesRepository resourcesRepository) {
        this.villageRepository = villageRepository;
        this.gridService = gridService;
        this.modelMapper = modelMapper;
        this.resourceService = resourceService;
        this.resourcesRepository = resourcesRepository;
    }

    // wtf this is the grid responsibility
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
        initializeDefaultBuildings(newVillage);

        villageRepository.save(newVillage);


        // Remove the selected spot from the list to avoid duplicates
        availableSpots.remove(randomIndex);

        return newVillage;
    }

    private void initializeDefaultBuildings(Village village) {
        List<Building> buildings = new ArrayList<>();

        for (BuildingType type : BuildingType.values()) {
            int numberOfBuildings = (type.ordinal() < 4) ? 4 : 1;
            for (int i = 0; i < numberOfBuildings; i++) {
                Building building = new Building(type, village);
                buildings.add(building);
            }
        }

        village.setBuildings(buildings);
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
        List<Village> villages = villageRepository.findByAccountUsername(username);

        if (villages.isEmpty()) {
            throw new RuntimeException("Village not found for account username: " + username);
        }

        Village village = villages.get(0);
        // Calculate resources produced during the elapsed hours
        resourceService.updateResourcesAndLastUpdated(village);

        // Save the updated village to the database
        villageRepository.save(village);


        return VillageMapper.toDTO(village);
    }

    public VillageDTO getVillageById(Long villageId) {
        Village village = villageRepository.findById(villageId)
                .orElseThrow(() -> new RuntimeException("Village not found with ID: " + villageId));

        // Calculate elapsed hours since the last update
        Date now = new Date();
        long elapsedHours = calculateElapsedHours(village.getLastUpdated(), now);

        // Calculate resources produced during the elapsed hours
        Map<String, Long> resourcesProduced = calculateResourcesProduced(village, elapsedHours);

        // Update the village's resources and lastUpdated time
        updateVillageResources(village, resourcesProduced, now);

        // Convert the updated village to DTO
        VillageDTO villageDTO = VillageMapper.toDTO(village);
        return villageDTO;
    }











    // this is resource 



    private long calculateElapsedHours(Date lastUpdated, Date now) {
        // Calculate the elapsed time in milliseconds
        long elapsedTimeInMillis = now.getTime() - lastUpdated.getTime();

        // Convert elapsed time from milliseconds to hours
        return elapsedTimeInMillis / (1000 * 60 * 60);
    }

    private Map<String, Long> calculateResourcesProduced(Village village, long elapsedHours) {
        Map<String, Long> resourcesProduced = new HashMap<>();

        // Iterate through the village's buildings to calculate resources produced by each building
        for (Building building : village.getBuildings()) {
            BigDecimal productionRate;
            if (building.getType() == BuildingType.PUB ||
                    building.getType() == BuildingType.BARRACKS ||
                    building.getType() == BuildingType.GRAIN_SILO ||
                    building.getType() == BuildingType.STABLE ||
                    building.getType() == BuildingType.RESEARCH_CENTER ||
                    building.getType() == BuildingType.STORAGE ||
                    building.getType() == BuildingType.SIEGE_WORKSHOP) {
                // For non-resource buildings, no need to calculate the production rate
             continue;
            } else {
                // For resource production buildings, calculate the production rate based on the level
                productionRate = building.getProductionRate().multiply(BigDecimal.valueOf(building.getLevel()));
            }

            String resourceType = getResourceTypeFromBuildingType(building.getType());

            // Calculate the total resources produced by the building during elapsed hours
            long totalProduced = productionRate.multiply(BigDecimal.valueOf(elapsedHours)).longValue();
            resourcesProduced.put(resourceType, totalProduced);
        }

        return resourcesProduced;
    }

    private void updateVillageResources(Village village, Map<String, Long> resourcesProduced, Date lastUpdated) {
        Resources resources = village.getResources();

        // Update wheat
        Long wheatProduced = resourcesProduced.getOrDefault("wheat", 0L);
        BigDecimal currentWheat = resources.getWheat();
        BigDecimal newWheat = currentWheat.add(BigDecimal.valueOf(wheatProduced));
        resources.setWheat(newWheat);

        // Update gold
        Long goldProduced = resourcesProduced.getOrDefault("gold", 0L);
        BigDecimal currentGold = resources.getGold();
        BigDecimal newGold = currentGold.add(BigDecimal.valueOf(goldProduced));
        resources.setGold(newGold);

        // Update wood
        Long woodProduced = resourcesProduced.getOrDefault("wood", 0L);
        BigDecimal currentWood = resources.getWood();
        BigDecimal newWood = currentWood.add(BigDecimal.valueOf(woodProduced));
        resources.setWood(newWood);

        // Update stone
        Long stoneProduced = resourcesProduced.getOrDefault("stone", 0L);
        BigDecimal currentStone = resources.getStone();
        BigDecimal newStone = currentStone.add(BigDecimal.valueOf(stoneProduced));
        resources.setStone(newStone);

        // Update the lastUpdated time
        village.setLastUpdated(lastUpdated);

        // Save the updated resources
        resourcesRepository.save(resources);
    }

    public void updateVillageResources(Village village) {
        Date now = new Date();
        long elapsedSeconds = calculateElapsedSeconds(village.getLastUpdated(), now);
        Map<String, Long> resourcesProduced = calculateResourcesProduced(village, elapsedSeconds);
        updateResources(village.getResources(), resourcesProduced);
        village.setLastUpdated(now);
        villageRepository.save(village);
    }

    private long calculateElapsedSeconds(Date lastUpdated, Date now) {
        long timeDifferenceInMillis = now.getTime() - lastUpdated.getTime();
        return timeDifferenceInMillis / 1000; // Convert milliseconds to seconds
    }

    private Map<String, Long> calculateResourcesProduced(Village village, long elapsedSeconds) {
        Map<String, Long> resourcesProduced = new HashMap<>();
        for (Building building : village.getBuildings()) {
            if (building.getType().isResourceBuilding()) {
                long productionRatePerSecond = building.calculateProductionRate().divide(BigDecimal.valueOf(3600), RoundingMode.HALF_UP).longValue();
                long producedResources = productionRatePerSecond * elapsedSeconds;
                String resourceName = building.getType().getResourceName();
                resourcesProduced.put(resourceName, resourcesProduced.getOrDefault(resourceName, 0L) + producedResources);
            }
        }
        return resourcesProduced;
    }

    // Utility method to map building type to resource type
    private String getResourceTypeFromBuildingType(BuildingType buildingType) {
        switch (buildingType) {
            case FARM:
                return "wheat";
            case FOREST:
                return "wood";
            case QUARRY:
                return "stone";
            case MINE:
                return "gold";
            default:
                throw new IllegalArgumentException("Invalid building type: " + buildingType);
        }
    }
}
