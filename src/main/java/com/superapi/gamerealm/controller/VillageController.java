package com.superapi.gamerealm.controller;

import com.superapi.gamerealm.dto.VillageDTO;
import com.superapi.gamerealm.dto.VillageMapper;
import com.superapi.gamerealm.model.Attack;
import com.superapi.gamerealm.model.Village;
import com.superapi.gamerealm.service.VillageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/villages")
public class VillageController {
    private final VillageService villageService;
    private final VillageMapper villageMapper;

    @Autowired
    public VillageController(VillageService villageService, VillageMapper villageMapper) {
        this.villageService = villageService;
        this.villageMapper = villageMapper;
    }

    @PostMapping
    public ResponseEntity<VillageDTO> createVillage(@RequestBody VillageDTO villageDto) {
        VillageDTO createdVillage = villageService.createVillage(villageDto);
        return new ResponseEntity<>(createdVillage, HttpStatus.CREATED);
    }

    @GetMapping("/{username}/")
    public ResponseEntity<VillageDTO> getVillageDetailsByUsername(@PathVariable String username) {
        VillageDTO village = villageService.getVillageWithDetailsByUsername(username);
        return ResponseEntity.ok(village);
    }

    @RequestMapping(value = "/update-name/{username}", method = RequestMethod.PUT)
    public ResponseEntity<VillageDTO> updateVillageName(@PathVariable String username, @RequestBody String newName) {
        Village updatedVillage = villageService.updateVillageName(username, newName);
        return ResponseEntity.ok(villageMapper.villageToVillageDTO(updatedVillage));
    }
    // Endpoint to get all villages
    @GetMapping
    public List<VillageDTO> getAllVillages() {
        return villageService.getAllVillages();
    }

    // Endpoint to get village by account username
    @GetMapping("/byAccountUsername/{username}")
    public VillageDTO getVillageByAccountUsername(@PathVariable String username) {
        return villageService.getVillageByAccountUsername(username);
    }

    @PostMapping("/{villageId}/declare-attack")
    public ResponseEntity<?> declareAttack(@PathVariable Long villageId, @RequestBody Attack attack) {
        Village attackerVillage = villageService.getVillage(villageId);
        attack.setAttackerVillage(attackerVillage);
        villageService.declareAttack(attack);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/{id}")
    public ResponseEntity<VillageDTO> getVillageById(@PathVariable Long id) {
        VillageDTO villageDTO = villageService.getVillageById(id);
        if (villageDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(villageDTO);
    }



    @GetMapping("/surrounding")
    public ResponseEntity<List<VillageDTO>> getSurroundingVillages(@RequestParam("x") int x, @RequestParam("y") int y) {
        List<VillageDTO> surroundingVillages = villageService.getSurroundingVillages(x, y);
        return new ResponseEntity<>(surroundingVillages, HttpStatus.OK);
    }





}
