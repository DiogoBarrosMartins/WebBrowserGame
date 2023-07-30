package com.superapi.gamerealm.controller;

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



    @GetMapping
    public ResponseEntity<Grid> getGrid() {
   return null;
    }



    @GetMapping("/villages")
    public ResponseEntity<List<Village>> getPlayerVillages() {
    return null;
    }

}

