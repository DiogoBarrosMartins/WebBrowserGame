package com.superapi.gamerealm.service;


import com.superapi.gamerealm.component.Coordinates;
import com.superapi.gamerealm.model.Grid;
import com.superapi.gamerealm.model.Village;
import com.superapi.gamerealm.repository.GridRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class GridService {

    private final GridRepository gridRepository;

    @Autowired
    public GridService(GridRepository gridRepository) {
        this.gridRepository = gridRepository;
    }


    public Grid initializeGrid() {
        int width = 5;
        int height = 5;

        Grid grid = new Grid(width, height);

        // Place a village at each coordinate
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Village village = new Village(x,y);
                village.setGrid(grid);
                grid.getVillages().add(village);
            }
        }

        // Save the grid with all villages
        return gridRepository.save(grid);
    }



    public Grid getGrid() {
        // You can add any additional logic here if needed before fetching the grid from the repository
        // For simplicity, we'll directly fetch the grid from the repository
        System.out.println("grid was called " + gridRepository.findById(1L).orElse(null));
        return gridRepository.findById(1L).orElse(null);
    }


    public Village getVillageAt(int x, int y) {
        Grid grid = getGrid();
        if (grid != null) {
            return grid.getVillages().stream()
                    .filter(village -> village.getCoordinates().getX() == x && village.getCoordinates().getY() == y)
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }

    // Constructors, getters, setters, etc.
    public Coordinates findAvailableSpotAroundCenter() {
        int centerX = 0;
        int centerY = 0;

        Grid grid = getGrid();
        if (grid != null) {
            for (int distance = 1; distance <= 5; distance++) {
                // Check the four cardinal directions (up, down, left, right) around the center
                if (isSpotAvailable(centerX + distance, centerY)) {
                    return new Coordinates(centerX + distance, centerY);
                } else if (isSpotAvailable(centerX - distance, centerY)) {
                    return new Coordinates(centerX - distance, centerY);
                } else if (isSpotAvailable(centerX, centerY + distance)) {
                    return new Coordinates(centerX, centerY + distance);
                } else if (isSpotAvailable(centerX, centerY - distance)) {
                    return new Coordinates(centerX, centerY - distance);
                }
            }
        }

        // Return null if no available spot is found within the given distance or if the grid is null
        return null;
    }

    public boolean isSpotAvailable(int x, int y) {
        // Check if the spot at coordinates (x, y) is available (i.e., no village is already there)
        return getGrid().getVillages().stream().noneMatch(village -> village.getCoordinates().getX() == x && village.getCoordinates().getY() == y);
    }

    public void saveGrid(Grid grid) {
        gridRepository.save(grid);
    }


    public void purgeEntireCity() {
        gridRepository.deleteAll();
    }

    public void addConquerableSpot() {

    }

}

