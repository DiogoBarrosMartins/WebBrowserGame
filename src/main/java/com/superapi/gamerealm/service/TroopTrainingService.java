package com.superapi.gamerealm.service;

import com.superapi.gamerealm.model.Village;
import com.superapi.gamerealm.model.buildings.Building;
import com.superapi.gamerealm.model.buildings.BuildingType;
import com.superapi.gamerealm.model.resources.TypeOfResource;
import com.superapi.gamerealm.model.troop.TroopTrainingQueue;
import com.superapi.gamerealm.model.troop.TroopType;
import com.superapi.gamerealm.model.troop.VillageTroops;
import com.superapi.gamerealm.repository.BuildingRepository;
import com.superapi.gamerealm.repository.TroopTrainingQueueRepository;
import com.superapi.gamerealm.repository.VillageRepository;
import com.superapi.gamerealm.repository.VillageTroopsRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class TroopTrainingService {

    private final TroopTrainingQueueRepository troopTrainingQueueRepository;
    private final VillageRepository villageRepository;
    private final VillageTroopsRepository villageTroopsRepository;
    private final BuildingRepository buildingRepository;
    private final ResourceService resourceService;

    public TroopTrainingService(TroopTrainingQueueRepository troopTrainingQueueRepository, VillageRepository villageRepository, VillageTroopsRepository villageTroopsRepository,BuildingRepository buildingRepository, ResourceService resourceService) {
        this.troopTrainingQueueRepository = troopTrainingQueueRepository;
        this.villageRepository = villageRepository;
        this.villageTroopsRepository = villageTroopsRepository;
        this.buildingRepository = buildingRepository;
        this.resourceService = resourceService;
    }

    private BuildingType getTrainingBuildingForTroopType(TroopType troopType) {
        // Determine the appropriate building based on troop type
        BuildingType buildingType;

        switch (troopType) {
            case HUMAN_FOOT_SOLDIER:
            case HUMAN_IMPERIAL_GUARD:
            case HUMAN_FOOT_KNIGHT:
            case HUMAN_FOOT_MAGES:
            case ORC_WARRIORS:
            case ORC_BRUTE_WARRIORS:
            case ORC_BLOODRAGE_BERSERKERS:
            case ORC_SHAMAN_WARRIORS:
            case ELVISH_SCOUTS:
            case ELVISH_FOREST_ARCHERS:
            case ELVISH_ELITE_RANGERS:
            case ELVISH_DRUID_WARRIORS:
                buildingType = BuildingType.BARRACKS;
                break;

            case HUMAN_ARCHER_CORPS:
            case HUMAN_LONG_BOWMEN:
            case HUMAN_CROSSBOWMEN:
            case ORC_SHADOW_ARCHERS:
            case ORC_POISON_BOWMEN:
            case ORC_EXPLOSIVE_ARCHERS:
            case ELVISH_WINDRIDER_ARCHERS:
            case ELVISH_SILVERLEAF_BOWMEN:
            case ELVISH_STORMRIDER_SNIPERS:
                buildingType = BuildingType.ARCHERY_RANGE;
                break;

            case HUMAN_CAVALRY_KNIGHTS:
            case HUMAN_LIGHT_CAVALRY:
            case HUMAN_ROYAL_LANCERS:
            case ORC_BLOODRIDERS:
            case ORC_WAR_BOARS:
            case ORC_WOLF_RAIDERS:
            case ELVISH_GRYPHON_KNIGHTS:
            case ELVISH_FOREST_RIDERS:
            case ELVISH_MOONSHADOW_DRAGOONS:
                buildingType = BuildingType.STABLE;
                break;

            case HUMAN_SIEGE_ENGINEERS:
            case HUMAN_CATAPULTS:
            case HUMAN_TREBUCHETS:
            case ORC_SIEGE_GOLEMS:
            case ORC_DEMOLISHERS:
            case ORC_LAVA_RAMMERS:
            case ELVISH_TREANT_SIEGE:
            case ELVISH_EARTHEN_CATAPULTS:
            case ELVISH_STARBREAKER_BALLISTAE:
                buildingType = BuildingType.SIEGE_WORKSHOP;
                break;

            default:
                // Unsupported troop type
                return null;
        }

        return buildingType;
    }


    @Transactional
    public void addTroopsToTrainingQueue(Long villageId, TroopType troopType, int quantity) {

        Village village = villageRepository.findById(villageId).orElse(null);
        assert village != null;
                BuildingType trainingBuilding = getTrainingBuildingForTroopType(troopType);
                Building building =  village.getBuildings().stream()
                        .filter(b -> b.getType() == trainingBuilding)
                        .findFirst()
                        .orElse(null);
                assert trainingBuilding != null;
                assert building != null;
                List<TroopType> availableTroops = getAvailableTroopsForBuilding(building.getId());

        // Check if the requested troopType is available for training in this building
        if (!availableTroops.contains(troopType)) {
            return;
        }



        Map<TypeOfResource, Double> totalCost = new HashMap<>();
        for (Map.Entry<TypeOfResource, Double> entry : troopType.getResourcesRequired().entrySet()) {
            TypeOfResource resourceType = entry.getKey();
            Double cost = entry.getValue();
            totalCost.put(resourceType, cost * quantity);
        }

        if (!resourceService.hasEnoughResources(villageId, totalCost)) {
            return;
        }

        // Deduct the resources from the village
        resourceService.deductResources(villageId, totalCost);
        // Adjust training time based on barracks level
        double reductionPercentagePerLevel = 0.02;  // 2% reduction for each level
        double adjustedTrainingTime = troopType.getTrainingTime() * (1 - reductionPercentagePerLevel * building.getLevel());

        // Calculate training start and end times
        LocalDateTime trainingStartTime = LocalDateTime.now();
        LocalDateTime trainingEndTime = trainingStartTime.plusSeconds((long) adjustedTrainingTime);

        // Use the adjustedTrainingTime for the troop training...

        List<TroopTrainingQueue> troopsToTrain = new ArrayList<>();
        for (int i = 0; i < quantity; i++) {
            TroopTrainingQueue queueEntry = new TroopTrainingQueue();
            queueEntry.setTroopType(troopType);
            queueEntry.setVillage(villageRepository.findById(villageId).orElseThrow());
            queueEntry.setTrainingStartTime(LocalDateTime.now());
            queueEntry.setTrainingEndTime(trainingEndTime);
            troopsToTrain.add(queueEntry);
            scheduleTrainingCompletion(queueEntry);
        }

        troopTrainingQueueRepository.saveAll(troopsToTrain);

    }

    public void processOverdueTroopTrainings(Long villageId) {
        List<TroopTrainingQueue> overdueTrainings = troopTrainingQueueRepository.findByVillageId(villageId);
        LocalDateTime now = LocalDateTime.now();

        for (TroopTrainingQueue training : overdueTrainings) {
            if (training.getTrainingEndTime().isBefore(now)) {
                completeTroopTraining(training);
            }

        }
    }

    @Transactional
    public void completeTroopTraining(TroopTrainingQueue troopTrainingQueue) {
        // Fetch the next troop training queue item that is overdue
        if (troopTrainingQueue.getTrainingEndTime().isBefore(LocalDateTime.now())) {
            System.out.println("training with ID " + troopTrainingQueue.getId() + " is ready for completion.");

            Village village = troopTrainingQueue.getVillage();
            VillageTroops villageTroops = village.getTroops().stream()
                    .filter(t -> t.getTroopType() == troopTrainingQueue.getTroopType())
                    .findFirst()
                    .orElseGet(() -> {
                        VillageTroops newTroops = new VillageTroops(village, troopTrainingQueue.getTroopType(), 0);
                        village.getTroops().add(newTroops);
                        return newTroops;
                    });

            // Increment the quantity of the troops by the number that was trained
            villageTroops.setQuantity(villageTroops.getQuantity() + 1);

            villageTroopsRepository.save(villageTroops);
            villageRepository.save(village);
            troopTrainingQueueRepository.delete(troopTrainingQueue);
            System.out.println("Training with ID " + troopTrainingQueue.getId() + " has been removed from the queue.");
        } else {
            System.out.println("Training with ID " + troopTrainingQueue.getId() + " is not yet ready for completion.");
        }
    }


    private boolean isValidTroopForRace(TroopType troopType, String playerRace) {
        String firstThreeCharsOfTroopType = troopType.name().substring(0, 3).toLowerCase();
        String firstThreeCharsOfPlayerRace = playerRace.length() >= 3 ? playerRace.substring(0, 3).toLowerCase() : playerRace.toLowerCase();

        System.out.println("First three characters of troop type: " + firstThreeCharsOfTroopType);
        System.out.println("First three characters of player race: " + firstThreeCharsOfPlayerRace);

        return firstThreeCharsOfTroopType.equals(firstThreeCharsOfPlayerRace);
    }





    public List<TroopType> getAvailableTroopsForBuilding(Long buildingId) {
        Building building = buildingRepository.findById(buildingId).orElse(null);
        if (building == null) {
            return List.of();  // Return an empty list if the building is not found
        }

        // Assume Building has an Account
        String playerRace =   building.getVillage().getAccount().getTribe();  // Assume Account has a race field
        BuildingType buildingType = building.getType();

        System.out.println("Method getAvailableTroopsForBuilding called with buildingId: " + buildingId);

        return Arrays.stream(TroopType.values())
                .peek(troop -> System.out.println("Initial troop: " + troop))
                .filter(troop -> isValidTroopForRace(troop, playerRace))
                .peek(troop -> System.out.println("After isValidTroopForRace: " + troop))
                .filter(troop -> canBeTrainedInBuilding(troop, buildingType))
                .peek(troop -> System.out.println("After canBeTrainedInBuilding: " + troop))
                .filter(troop -> isBuildingLevelSufficientForTroop(building, troop))
                .peek(troop -> System.out.println("After isBuildingLevelSufficientForTroop: " + troop))
                .collect(Collectors.toList());
    }

    private boolean canBeTrainedInBuilding(TroopType troop, BuildingType buildingType) {
        return switch (buildingType) {
            case BARRACKS -> Arrays.asList(TroopType.HUMAN_FOOT_SOLDIER, TroopType.HUMAN_IMPERIAL_GUARD,
                    TroopType.HUMAN_FOOT_KNIGHT, TroopType.HUMAN_FOOT_MAGES,
                    TroopType.ORC_WARRIORS, TroopType.ORC_BRUTE_WARRIORS,
                    TroopType.ORC_BLOODRAGE_BERSERKERS, TroopType.ORC_SHAMAN_WARRIORS,
                    TroopType.ELVISH_SCOUTS, TroopType.ELVISH_FOREST_ARCHERS,
                    TroopType.ELVISH_ELITE_RANGERS, TroopType.ELVISH_DRUID_WARRIORS).contains(troop);
            case ARCHERY_RANGE -> Arrays.asList(TroopType.HUMAN_ARCHER_CORPS, TroopType.HUMAN_LONG_BOWMEN,
                    TroopType.HUMAN_CROSSBOWMEN, TroopType.ORC_SHADOW_ARCHERS,
                    TroopType.ORC_POISON_BOWMEN, TroopType.ORC_EXPLOSIVE_ARCHERS,
                    TroopType.ELVISH_WINDRIDER_ARCHERS, TroopType.ELVISH_SILVERLEAF_BOWMEN,
                    TroopType.ELVISH_STORMRIDER_SNIPERS).contains(troop);
            case STABLE -> Arrays.asList(TroopType.HUMAN_CAVALRY_KNIGHTS, TroopType.HUMAN_LIGHT_CAVALRY,
                    TroopType.HUMAN_ROYAL_LANCERS, TroopType.ORC_BLOODRIDERS,
                    TroopType.ORC_WAR_BOARS, TroopType.ORC_WOLF_RAIDERS,
                    TroopType.ELVISH_GRYPHON_KNIGHTS, TroopType.ELVISH_FOREST_RIDERS,
                    TroopType.ELVISH_MOONSHADOW_DRAGOONS).contains(troop);
            case SIEGE_WORKSHOP -> Arrays.asList(TroopType.HUMAN_SIEGE_ENGINEERS, TroopType.HUMAN_CATAPULTS,
                    TroopType.HUMAN_TREBUCHETS, TroopType.ORC_SIEGE_GOLEMS,
                    TroopType.ORC_DEMOLISHERS, TroopType.ORC_LAVA_RAMMERS,
                    TroopType.ELVISH_TREANT_SIEGE, TroopType.ELVISH_EARTHEN_CATAPULTS,
                    TroopType.ELVISH_STARBREAKER_BALLISTAE).contains(troop);
            default -> false;
        };
    }


    private boolean isBuildingLevelSufficientForTroop(Building building, TroopType troopType) {
        // Check if the building level is sufficient for training the troop
        int requiredLevel;
        switch (troopType) {
            case HUMAN_FOOT_SOLDIER:
            case ORC_WARRIORS:
            case ELVISH_SCOUTS, HUMAN_ARCHER_CORPS, ORC_SHADOW_ARCHERS, ELVISH_WINDRIDER_ARCHERS, HUMAN_CAVALRY_KNIGHTS, ORC_BLOODRIDERS, ELVISH_GRYPHON_KNIGHTS, HUMAN_SIEGE_ENGINEERS, ORC_SIEGE_GOLEMS, ELVISH_TREANT_SIEGE:
                requiredLevel = 1;
                break;
            case HUMAN_IMPERIAL_GUARD:
            case ORC_BRUTE_WARRIORS:
            case ELVISH_FOREST_ARCHERS, HUMAN_LONG_BOWMEN, ORC_POISON_BOWMEN, ELVISH_SILVERLEAF_BOWMEN, HUMAN_LIGHT_CAVALRY, ORC_WAR_BOARS, ELVISH_FOREST_RIDERS, HUMAN_CATAPULTS, ORC_DEMOLISHERS, ELVISH_EARTHEN_CATAPULTS:
                requiredLevel = 2;
                break;
            case HUMAN_FOOT_KNIGHT:
            case ORC_BLOODRAGE_BERSERKERS:
            case ELVISH_ELITE_RANGERS, HUMAN_CROSSBOWMEN, ORC_EXPLOSIVE_ARCHERS, ELVISH_STORMRIDER_SNIPERS, HUMAN_ROYAL_LANCERS, ORC_WOLF_RAIDERS, ELVISH_MOONSHADOW_DRAGOONS, HUMAN_TREBUCHETS, ORC_LAVA_RAMMERS, ELVISH_STARBREAKER_BALLISTAE:
                requiredLevel = 3;
                break;
            case HUMAN_FOOT_MAGES:
            case ORC_SHAMAN_WARRIORS:
            case ELVISH_DRUID_WARRIORS:
                requiredLevel = 4;
                break;
            default:
                // Unsupported troop type
                return false;
        }

        return building.getLevel() >= requiredLevel;
    }
    private void scheduleTrainingCompletion(TroopTrainingQueue troopTrainingQueue) {

        long delay = Duration.between(LocalDateTime.now(), troopTrainingQueue.getTrainingEndTime()).toMillis();
        System.out.println("Scheduling Training completion with a delay of: " + delay + " milliseconds.");
        CompletableFuture.delayedExecutor(delay, TimeUnit.MILLISECONDS)
                .execute(() -> completeTroopTraining(troopTrainingQueue));
    }

    public List<TroopTrainingQueue> getTrainingQueueForVillage(Long villageId) {
        return troopTrainingQueueRepository.findByVillageId(villageId);
    }


}

