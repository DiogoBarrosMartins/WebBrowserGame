package com.superapi.gamerealm.service;


import com.superapi.gamerealm.component.Coordinates;
import com.superapi.gamerealm.model.Grid;
import com.superapi.gamerealm.model.Village;
import com.superapi.gamerealm.repository.GridRepository;
import com.superapi.gamerealm.repository.VillageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class GridService {


    private final GridRepository gridRepository;
    private final VillageRepository villageRepository;

    @Autowired
    public GridService(GridRepository gridRepository, VillageRepository villageRepository) {
        this.gridRepository = gridRepository;
        this.villageRepository = villageRepository;
    }


    public Grid initializeGrid() {
        int width = 5;
        int height = 5;

        Grid grid = new Grid(width, height);

        // Place a village at each coordinate
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Coordinates coordinates = new Coordinates(x, y);
                grid.getVillageCoordinates().add(coordinates);
            }
        }

        // Save the grid with all village coordinates
        return gridRepository.save(grid);
    }

    // Will only get the first GRID, we dont need it any other way. for now.
    public Grid getGrid() {
        System.out.println("grid was called " + gridRepository.findById(1L).orElse(null));
        return gridRepository.findById(1L).orElse(null);
    }



    public Village getVillageAt(int x, int y) {
        Grid grid = getGrid();
        if (grid != null) {
            Coordinates coordinates = grid.getVillageCoordinates().stream()
                    .filter(coord -> coord.getX() == x && coord.getY() == y)
                    .findFirst()
                    .orElse(null);

            if (coordinates != null && coordinates.hasVillage()) {
                return villageRepository.findByCoordinatesXAndCoordinatesY(x,y);
            }
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
        return getGrid().getVillageCoordinates().stream().noneMatch(Coordinates::hasVillage);
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

