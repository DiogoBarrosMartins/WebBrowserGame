package com.superapi.gamerealm.service;

import com.superapi.gamerealm.model.Village;
import com.superapi.gamerealm.model.troop.TroopTrainingQueue;
import com.superapi.gamerealm.model.troop.TroopType;
import com.superapi.gamerealm.repository.TroopTrainingQueueRepository;
import com.superapi.gamerealm.repository.VillageRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TroopTrainingService {

    private final TroopTrainingQueueRepository troopTrainingQueueRepository;

    private final VillageRepository villageRepository;

    // Start training troops


    public TroopTrainingService(TroopTrainingQueueRepository troopTrainingQueueRepository, VillageRepository villageRepository) {
        this.troopTrainingQueueRepository = troopTrainingQueueRepository;
        this.villageRepository = villageRepository;
    }

    public void startTraining(Long villageId, Long troopTypeId, int quantity) {
        Village village = villageRepository.findById(villageId).orElseThrow();

        // Check available resources and deduct them
        // (similar to building upgrade resource check)

        // Calculate training time based on troop type and quantity
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime trainingEndTime = calculateTrainingEndTime(troopTypeId, quantity);

        TroopTrainingQueue trainingQueue = new TroopTrainingQueue();
        trainingQueue.setVillage(village);
        trainingQueue.setTroopType(new TroopType(troopTypeId));
        trainingQueue.setQuantity(quantity);
        trainingQueue.setTrainingStartTime(now);
        trainingQueue.setTrainingEndTime(trainingEndTime);

        troopTrainingQueueRepository.save(trainingQueue);
    }

    // Collect trained troops
    public void collectTrainedTroops(Long villageId) {
        List<TroopTrainingQueue> completedTrainings = troopTrainingQueueRepository.findByVillageIdAndTrainingEndTimeBefore(villageId, LocalDateTime.now());

        for (TroopTrainingQueue completedTraining : completedTrainings) {
            // Logic to add the trained troops to the village's troop count
            // ...

            // Remove the completed training from the queue
            troopTrainingQueueRepository.delete(completedTraining);
        }
    }

    private LocalDateTime calculateTrainingEndTime(Long troopTypeId, int quantity) {
        // Logic to calculate training end time based on troop type and quantity
        // ...

        return endTime;
    }

    // Additional service methods...
}

