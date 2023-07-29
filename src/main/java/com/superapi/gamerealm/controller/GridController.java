package com.superapi.gamerealm.controller;

import com.superapi.gamerealm.model.BarbarianVillage;
import com.superapi.gamerealm.model.ConquerableSpot;
import com.superapi.gamerealm.model.Grid;
import com.superapi.gamerealm.model.Village;
import com.superapi.gamerealm.service.GridService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/grid")
public class GridController {

    private final GridService gridService;

    @Autowired
    public GridController(GridService gridService) {
        this.gridService = gridService;
    }

    // FORBIDDEN XDD

    @PostMapping("/generate")
    public ResponseEntity<String> generateInitialGrid() {
       System.out.println("LETS HOPE WE DONT SEE THIS MESSAGE");
        gridService.createAndInitializeGrid();
        return ResponseEntity.ok("Initial grid generation complete.");
    }


    @GetMapping
    public ResponseEntity<Grid> getGrid() {
        Grid grid = gridService.getGrid();
        return ResponseEntity.ok(grid);
    }

    @GetMapping("/conquerableSpots")
    public ResponseEntity<List<ConquerableSpot>> getConquerableSpots() {
        List<ConquerableSpot> conquerableSpots = gridService.getConquerableSpots();
        return ResponseEntity.ok(conquerableSpots);
    }


    @GetMapping("/barbarians")
    public ResponseEntity<List<BarbarianVillage>> getBarbarianVillages() {
        List<BarbarianVillage> barbarianVillages = gridService.getGrid().getBarbarianVillages();
        return ResponseEntity.ok(barbarianVillages);
    }
    @GetMapping("/villages")
    public ResponseEntity<List<Village>> getPlayerVillages() {
        List<Village> playerVillages = gridService.getGrid().getPlayerVillages();
        return ResponseEntity.ok(playerVillages);
    }

}

