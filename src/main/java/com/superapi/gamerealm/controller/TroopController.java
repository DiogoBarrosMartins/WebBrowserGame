package com.superapi.gamerealm.controller;

import com.superapi.gamerealm.model.troop.Troop;
import com.superapi.gamerealm.service.TroopServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/troops")
public class TroopController {

    private final TroopServiceImpl troopService;

    public TroopController(TroopServiceImpl troopService) {
        this.troopService = troopService;
    }

    @GetMapping
    public ResponseEntity<List<Troop>> getAvailableTroops() {
        return ResponseEntity.ok(troopService.getAvailableTroops());
    }

    @PostMapping("/train/{type}")
    public ResponseEntity<String> trainTroop(@PathVariable String type) {
        // Logic to convert 'type' to actual Troop class
        return ResponseEntity.ok("Training initiated");
    }
}
