package com.superapi.gamerealm.controller;

import com.superapi.gamerealm.dto.VillageDTO;
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

    @Autowired
    public VillageController(VillageService villageService) {
        this.villageService = villageService;
    }

    @PostMapping
    public ResponseEntity<VillageDTO> createVillage(@RequestBody VillageDTO villageDTO) {
        VillageDTO createdVillage = villageService.createVillage(villageDTO);
        return new ResponseEntity<>(createdVillage, HttpStatus.CREATED);
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



    @GetMapping("/{id}")
    public ResponseEntity<VillageDTO> getVillageById(@PathVariable Long id) {
        VillageDTO villageDTO = villageService.getVillageById(id);
        if (villageDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(villageDTO);
    }
}
