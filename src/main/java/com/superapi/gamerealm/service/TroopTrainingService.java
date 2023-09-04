package com.superapi.gamerealm.service;

import com.superapi.gamerealm.model.Village;
import com.superapi.gamerealm.model.buildings.Building;
import com.superapi.gamerealm.model.buildings.BuildingType;
import com.superapi.gamerealm.model.resources.TypeOfResource;
import com.superapi.gamerealm.model.troop.TroopTrainingQueue;
import com.superapi.gamerealm.model.troop.TroopType;
import com.superapi.gamerealm.model.troop.VillageTroops;
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

@Service
public class TroopTrainingService {

    private final TroopTrainingQueueRepository troopTrainingQueueRepository;
    private final VillageRepository villageRepository;
    private final VillageTroopsRepository villageTroopsRepository;

    private final ResourceService resourceService;

    public TroopTrainingService(TroopTrainingQueueRepository troopTrainingQueueRepository, VillageRepository villageRepository, VillageTroopsRepository villageTroopsRepository, ResourceService resourceService) {
        this.troopTrainingQueueRepository = troopTrainingQueueRepository;
        this.villageRepository = villageRepository;
        this.villageTroopsRepository = villageTroopsRepository;
        this.resourceService = resourceService;
    }


    @Transactional
    public void addTroopsToTrainingQueue(Long villageId, TroopType troopType, int quantity) {


        Village village = villageRepository.findById(villageId).orElse(null);
        assert village != null;
        String playerRace = village.getAccount().getTribe();

        // Check if the player's race matches the troop's race
        if (!isValidTroopForRace(troopType, playerRace)) {
            return;
        }
        // Check if the building level is sufficient for training
        // Fetch the appropriate building based on troop type
        Building trainingBuilding = getTrainingBuildingForTroopType(troopType, village);
        if (trainingBuilding == null) {
            // Building not found for troop type
            return;
        }

        // Check if the building level is sufficient for training
        if (!isBuildingLevelSufficientForTroop(trainingBuilding, troopType)) {
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
        double adjustedTrainingTime = troopType.getTrainingTime() * (1 - reductionPercentagePerLevel * trainingBuilding.getLevel());

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
        // Check if the player's race matches the troop's race
        return switch (playerRace) {
            case "human" -> troopType.ordinal() < TroopType.HUMAN_FOOT_SOLDIER.ordinal() + 4; // 4 types of foot troops
            case "orc" -> troopType.ordinal() >= TroopType.ORC_WARRIORS.ordinal()
                    && troopType.ordinal() < TroopType.ORC_WARRIORS.ordinal() + 4; // 4 types of foot troops
            case "elf" -> troopType.ordinal() >= TroopType.ELF_SCOUTS.ordinal()
                    && troopType.ordinal() < TroopType.ELF_SCOUTS.ordinal() + 4; // 4 types of foot troops
            default -> false; // Unsupported player race
        };
    }

    private Building getTrainingBuildingForTroopType(TroopType troopType, Village village) {
        // Determine the appropriate building based on troop type
        BuildingType buildingType;
        switch (troopType) {
            case HUMAN_FOOT_SOLDIER:
            case HUMAN_IMPERIAL_GUARD:
            case HUMAN_FOOT_KNIGHT:
            case HUMAN_FOOT_MAGES, ORC_WARRIORS, ORC_BRUTE_WARRIORS, ORC_BLOODRAGE_BERSERKERS, ORC_SHAMAN_WARRIORS, ELF_SCOUTS, ELF_FOREST_ARCHERS, ELF_ELITE_RANGERS, ELF_DRUID_WARRIORS:
                buildingType = BuildingType.BARRACKS;
                break;
            case HUMAN_ARCHER_CORPS:
            case HUMAN_LONG_BOWMEN:
            case HUMAN_CROSSBOWMEN, ORC_SHADOW_ARCHERS, ORC_POISON_BOWMEN, ORC_EXPLOSIVE_ARCHERS, ELF_WINDRIDER_ARCHERS, ELF_SILVERLEAF_BOWMEN, ELF_STORMRIDER_SNIPERS:
                buildingType = BuildingType.ARCHERY_RANGE;
                break;
            case HUMAN_CAVALRY_KNIGHTS:
            case HUMAN_LIGHT_CAVALRY:
            case HUMAN_ROYAL_LANCERS, ORC_BLOODRIDERS, ORC_WAR_BOARS, ORC_WOLF_RAIDERS, ELF_GRYPHON_KNIGHTS, ELF_FOREST_RIDERS, ELF_MOONSHADOW_DRAGOONS:
                buildingType = BuildingType.STABLE;
                break;
            case HUMAN_SIEGE_ENGINEERS:
            case HUMAN_CATAPULTS:
            case HUMAN_TREBUCHETS, ORC_SIEGE_GOLEMS, ORC_DEMOLISHERS, ORC_LAVA_RAMMERS, ELF_TREANT_SIEGE, ELF_EARTHEN_CATAPULTS, ELF_STARBREAKER_BALLISTAE:
                buildingType = BuildingType.SIEGE_WORKSHOP;
                break;
            default:
                // Unsupported troop type
                return null;
        }

        // Find the building of the specified type in the village
        return village.getBuildings().stream()
                .filter(b -> b.getType() == buildingType)
                .findFirst()
                .orElse(null);
    }

    private boolean isBuildingLevelSufficientForTroop(Building building, TroopType troopType) {
        // Check if the building level is sufficient for training the troop
        int requiredLevel;
        switch (troopType) {
            case HUMAN_FOOT_SOLDIER:
            case ORC_WARRIORS:
            case ELF_SCOUTS, HUMAN_ARCHER_CORPS, ORC_SHADOW_ARCHERS, ELF_WINDRIDER_ARCHERS, HUMAN_CAVALRY_KNIGHTS, ORC_BLOODRIDERS, ELF_GRYPHON_KNIGHTS, HUMAN_SIEGE_ENGINEERS, ORC_SIEGE_GOLEMS, ELF_TREANT_SIEGE:
                requiredLevel = 1;
                break;
            case HUMAN_IMPERIAL_GUARD:
            case ORC_BRUTE_WARRIORS:
            case ELF_FOREST_ARCHERS, HUMAN_LONG_BOWMEN, ORC_POISON_BOWMEN, ELF_SILVERLEAF_BOWMEN, HUMAN_LIGHT_CAVALRY, ORC_WAR_BOARS, ELF_FOREST_RIDERS, HUMAN_CATAPULTS, ORC_DEMOLISHERS, ELF_EARTHEN_CATAPULTS:
                requiredLevel = 2;
                break;
            case HUMAN_FOOT_KNIGHT:
            case ORC_BLOODRAGE_BERSERKERS:
            case ELF_ELITE_RANGERS, HUMAN_CROSSBOWMEN, ORC_EXPLOSIVE_ARCHERS, ELF_STORMRIDER_SNIPERS, HUMAN_ROYAL_LANCERS, ORC_WOLF_RAIDERS, ELF_MOONSHADOW_DRAGOONS, HUMAN_TREBUCHETS, ORC_LAVA_RAMMERS, ELF_STARBREAKER_BALLISTAE:
                requiredLevel = 3;
                break;
            case HUMAN_FOOT_MAGES:
            case ORC_SHAMAN_WARRIORS:
            case ELF_DRUID_WARRIORS:
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

    public List<TroopType> getAllTroopTypes() {     // Return all troop types defined in the TroopType enum
        return Arrays.asList(TroopType.values());
    }
}
