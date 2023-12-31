package com.superapi.gamerealm.controller;

import com.superapi.gamerealm.dto.TroopTrainingQueueDTO;
import com.superapi.gamerealm.dto.troops.TrainTroopsRequest;
import com.superapi.gamerealm.dto.troops.TroopMapper;
import com.superapi.gamerealm.dto.troops.TroopTypeDTO;
import com.superapi.gamerealm.model.troop.TroopTrainingQueue;
import com.superapi.gamerealm.model.troop.TroopType;
import com.superapi.gamerealm.service.TroopTrainingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/troops")
public class TroopController {

    private final TroopTrainingService troopTrainingService;


    public TroopController(TroopTrainingService troopTrainingService) {
        this.troopTrainingService = troopTrainingService;
    }

    // Endpoint to train a specific type of troop
    @PostMapping("/train/{villageId}")
    public ResponseEntity<Void> trainTroops(@PathVariable Long villageId,
                                            @RequestBody TrainTroopsRequest trainTroopsRequest) {
        troopTrainingService.addTroopsToTrainingQueue(villageId, trainTroopsRequest.getTroopType(), trainTroopsRequest.getQuantity());
        return ResponseEntity.ok().build();
    }


    // Endpoint to check the training queue for a village
    @GetMapping("/training-queue/{villageId}")
    public ResponseEntity<List<TroopTrainingQueueDTO>> getTrainingQueue(@PathVariable Long villageId) {
        List<TroopTrainingQueue> queue = troopTrainingService.getTrainingQueueForVillage(villageId);
        List<TroopTrainingQueueDTO> dtoList = queue.stream()
                .map(TroopMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/{buildingId}/list")
    public List<TroopTypeDTO> getAllTroopTypes(@PathVariable Long buildingId) {
        // Fetch all available troop types for the given building from your service
        List<TroopType> troopTypes = troopTrainingService.getAvailableTroopsForBuilding(buildingId);

        // Convert TroopType entities to TroopTypeDTOs
        return troopTypes.stream()
                .map(TroopMapper::toDTO)
                .collect(Collectors.toList());
    }

}
