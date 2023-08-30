package com.superapi.gamerealm.service;

import com.superapi.gamerealm.model.Village;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public boolean addTroopsToTrainingQueue(Long villageId, TroopType troopType, int quantity) {
        // Check if the village has enough resources to train the specified number of troops
        Map<TypeOfResource, Double> totalCost = new HashMap<>();
        troopType.getResourcesRequired().forEach((resourceType, cost) -> {
            totalCost.put(resourceType, cost * quantity);
        });

        if (!resourceService.hasEnoughResources(villageId, totalCost)) {
            return false;
        }

        // Deduct the resources from the village
        resourceService.deductResources(villageId, totalCost);

        List<TroopTrainingQueue> troopsToTrain = new ArrayList<>();
        for (int i = 0; i < quantity; i++) {
            TroopTrainingQueue queueEntry = new TroopTrainingQueue();
            queueEntry.setTroopType(troopType);
            queueEntry.setVillage(villageRepository.findById(villageId).orElseThrow());
            queueEntry.setTrainingStartTime(LocalDateTime.now());
            queueEntry.setTrainingEndTime(LocalDateTime.now().plusSeconds((long) troopType.getTrainingTime() * (i + 1)));
            troopsToTrain.add(queueEntry);
            scheduleTrainingCompletion(queueEntry);
        }

        troopTrainingQueueRepository.saveAll(troopsToTrain);

        return true;
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
            // Remove construction from the queue

            System.out.println("Training with ID " + troopTrainingQueue.getId() + " has been removed from the queue.");
        } else {
            System.out.println("Training with ID " + troopTrainingQueue.getId() + " is not yet ready for completion.");
        }
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
