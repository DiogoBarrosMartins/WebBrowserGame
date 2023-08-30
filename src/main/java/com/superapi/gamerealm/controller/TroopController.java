package com.superapi.gamerealm.controller;

import com.superapi.gamerealm.dto.TrainTroopsRequest;
import com.superapi.gamerealm.dto.TroopMapper;
import com.superapi.gamerealm.dto.TroopTrainingQueueDTO;
import com.superapi.gamerealm.model.troop.TroopTrainingQueue;
import com.superapi.gamerealm.repository.TroopTrainingQueueRepository;
import com.superapi.gamerealm.service.TroopTrainingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/villages/{villageId}/troops")
public class TroopController {

    private final TroopTrainingService troopTrainingService;

    private final TroopTrainingQueueRepository troopTrainingQueueRepository;

    public TroopController(TroopTrainingService troopTrainingService, TroopTrainingQueueRepository troopTrainingQueueRepository) {
        this.troopTrainingService = troopTrainingService;
        this.troopTrainingQueueRepository = troopTrainingQueueRepository;
    }

    // Endpoint to train a specific type of troop
    @PostMapping("/train")
    public ResponseEntity<Void> trainTroops(@PathVariable Long villageId,
                                            @RequestBody TrainTroopsRequest trainTroopsRequest) {
        troopTrainingService.addTroopsToTrainingQueue(villageId, trainTroopsRequest.getTroopType(), trainTroopsRequest.getQuantity());
        return ResponseEntity.ok().build();
    }


    // Endpoint to check the training queue for a village
    @GetMapping("/training-queue")
    public ResponseEntity<List<TroopTrainingQueueDTO>> getTrainingQueue(@PathVariable Long villageId) {
        List<TroopTrainingQueue> queue = troopTrainingService.getTrainingQueueForVillage(villageId);
        List<TroopTrainingQueueDTO> dtoList = queue.stream()
                .map(TroopMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }


}
