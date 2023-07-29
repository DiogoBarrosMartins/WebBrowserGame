package com.superapi.gamerealm.controller;

import com.superapi.gamerealm.dto.VillageDTO;
import com.superapi.gamerealm.service.VillageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

    // Other village-related endpoints
}
