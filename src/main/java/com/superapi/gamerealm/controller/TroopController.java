package com.superapi.gamerealm.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/troops")
public class TroopController {


    @PostMapping("/train/{type}")
    public ResponseEntity<String> trainTroop(@PathVariable String type) {
        // Logic to convert 'type' to actual Troop class
        return ResponseEntity.ok("Training initiated");
    }
}
