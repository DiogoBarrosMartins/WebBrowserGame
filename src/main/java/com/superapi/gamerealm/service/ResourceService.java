package com.superapi.gamerealm.service;

import com.superapi.gamerealm.model.Village;
import com.superapi.gamerealm.model.buildings.BuildingType;
import com.superapi.gamerealm.model.resources.Resources;
import com.superapi.gamerealm.model.resources.TypeOfResource;
import com.superapi.gamerealm.repository.ResourcesRepository;
import com.superapi.gamerealm.repository.VillageRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
@Service
public class ResourceService {
    private static final TypeOfResource[] RESOURCE_TYPES = {
            TypeOfResource.WOOD, TypeOfResource.WHEAT, TypeOfResource.STONE, TypeOfResource.GOLD
    };

    private final ResourcesRepository resourcesRepository;
    private final VillageRepository villageRepository;

    public ResourceService(ResourcesRepository resourcesRepository, VillageRepository villageRepository) {
        this.resourcesRepository = resourcesRepository;
        this.villageRepository = villageRepository;
    }

    public Map<TypeOfResource, Double> getAvailableResources(Long villageId) {
        Village village = getVillageById(villageId);
        Resources resources = village.getResources().get(0);
        Map<TypeOfResource, Double> availableResources = new HashMap<>();
        for (TypeOfResource type : RESOURCE_TYPES) {
            switch (type) {
                case WOOD:
                    availableResources.put(TypeOfResource.WOOD, resources.getWood());
                    break;
                case WHEAT:
                    availableResources.put(TypeOfResource.WHEAT, resources.getWheat());
                    break;
                case STONE:
                    availableResources.put(TypeOfResource.STONE, resources.getStone());
                    break;
                case GOLD:
                    availableResources.put(TypeOfResource.GOLD, resources.getGold());
                    break;
            }
        }
        return availableResources;
    }

    @Transactional
    public void updateVillageResources(Village village) {
        synchronized(village) {
            Resources resources = village.getResources().get(0);
            LocalDateTime lastUpdated = village.getLastUpdated();
            LocalDateTime now = LocalDateTime.now();
            long minutesElapsed = Duration.between(lastUpdated, now).toMinutes();

            for (TypeOfResource type : RESOURCE_TYPES) {
                double productionRate = calculateProductionRate(village, type);
                double totalProduced = productionRate * minutesElapsed;

                switch (type) {
                    case WOOD:
                        resources.setWood(resources.getWood() + totalProduced);
                        break;
                    case WHEAT:
                        resources.setWheat(resources.getWheat() + totalProduced);
                        break;
                    case STONE:
                        resources.setStone(resources.getStone() + totalProduced);
                        break;
                    case GOLD:
                        resources.setGold(resources.getGold() + totalProduced);
                        break;
                }
            }

            resourcesRepository.save(resources);
            village.setLastUpdated(now);
            villageRepository.save(village);
        }
    }

    public boolean hasEnoughResources(Long villageId, Map<TypeOfResource, Double> resourcesNeeded) {
        Village village = getVillageById(villageId);
        Resources resources = village.getResources().get(0);

        for (TypeOfResource type : RESOURCE_TYPES) {
            if (resourcesNeeded.containsKey(type)) {
                double needed = resourcesNeeded.get(type);
                switch (type) {
                    case WOOD:
                        if (resources.getWood() < needed) return false;
                        break;
                    case WHEAT:
                        if (resources.getWheat() < needed) return false;
                        break;
                    case STONE:
                        if (resources.getStone() < needed) return false;
                        break;
                    case GOLD:
                        if (resources.getGold() < needed) return false;
                        break;
                }
            }
        }
        return true;
    }

    @Transactional
    public void deductResources(Long villageId, Map<TypeOfResource, Double> resourcesToDeduct) {
        Village village = getVillageById(villageId);
        Resources resources = village.getResources().get(0);

        for (TypeOfResource type : RESOURCE_TYPES) {
            if (resourcesToDeduct.containsKey(type)) {
                double toDeduct = resourcesToDeduct.get(type);
                switch (type) {
                    case WOOD:
                        if (resources.getWood() >= toDeduct) {
                            resources.setWood(resources.getWood() - toDeduct);
                        } else {
                            throw new RuntimeException("Not enough wood to deduct");
                        }
                        break;
                    case WHEAT:
                        if (resources.getWheat() >= toDeduct) {
                            resources.setWheat(resources.getWheat() - toDeduct);
                        } else {
                            throw new RuntimeException("Not enough wheat to deduct");
                        }
                        break;
                    case STONE:
                        if (resources.getStone() >= toDeduct) {
                            resources.setStone(resources.getStone() - toDeduct);
                        } else {
                            throw new RuntimeException("Not enough stone to deduct");
                        }
                        break;
                    case GOLD:
                        if (resources.getGold() >= toDeduct) {
                            resources.setGold(resources.getGold() - toDeduct);
                        } else {
                            throw new RuntimeException("Not enough gold to deduct");
                        }
                        break;
                }
            }
        }

        resourcesRepository.save(resources);
    }

    private Village getVillageById(Long villageId) {
        return villageRepository.findById(villageId).orElseThrow(() -> new RuntimeException("Village not found"));
    }

    private double calculateProductionRate(Village village, TypeOfResource type) {
        return village.getBuildings().stream()
                .filter(building -> getBuildingTypeForResource(type) == building.getType())
                .mapToDouble(building -> building.getProductionRate().doubleValue())
                .sum();
    }

    private BuildingType getBuildingTypeForResource(TypeOfResource type) {
        switch (type) {
            case WOOD:
                return BuildingType.FOREST;
            case WHEAT:
                return BuildingType.FARM;
            case STONE:
                return BuildingType.QUARRY;
            case GOLD:
                return BuildingType.MINE;
            default:
                throw new IllegalArgumentException("Invalid resource type");
        }
    }
}
