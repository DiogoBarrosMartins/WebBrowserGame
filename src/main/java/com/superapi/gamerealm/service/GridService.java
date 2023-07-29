package com.superapi.gamerealm.service;


import com.superapi.gamerealm.model.BarbarianVillage;
import com.superapi.gamerealm.model.ConquerableSpot;
import com.superapi.gamerealm.model.Grid;
import com.superapi.gamerealm.repository.GridRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class GridService {

    private final GridRepository gridRepository;

    @Autowired
    public GridService(GridRepository gridRepository) {
        this.gridRepository = gridRepository;
    }
    public Grid createAndInitializeGrid() {
        Grid grid = new Grid(5, 5); // Create a 5x5 grid

        // Add 5 barbarian villages to the grid at random positions
        addRandomBarbarianVillages(grid, 5);

        // Add 20 conquerable spots to the grid
        addConquerableSpots(grid, 20);

        gridRepository.save(grid); // Save the grid to the database
        return grid;
    }

    private void addRandomBarbarianVillages(Grid grid, int numBarbarianVillages) {
        int width = grid.getWidth();
        int height = grid.getHeight();

        // Ensure that no two spots have the same coordinates
        Set<String> occupiedSpots = new HashSet<>();

        for (int i = 0; i < numBarbarianVillages; i++) {
            int xCoordinate = generateRandomCoordinate(width);
            int yCoordinate = generateRandomCoordinate(height);

            // Generate a unique key for this spot
            String spotKey = xCoordinate + "," + yCoordinate;

            // If the spot is already occupied, generate new coordinates until it's unique
            while (occupiedSpots.contains(spotKey)) {
                xCoordinate = generateRandomCoordinate(width);
                yCoordinate = generateRandomCoordinate(height);
                spotKey = xCoordinate + "," + yCoordinate;
            }

            BarbarianVillage barbarianVillage = new BarbarianVillage();
            barbarianVillage.setXCoordinate(xCoordinate);
            barbarianVillage.setYCoordinate(yCoordinate);
            grid.getBarbarianVillages().add(barbarianVillage);

            // Mark the spot as occupied
            occupiedSpots.add(spotKey);
        }
    }

    private void addConquerableSpots(Grid grid, int numConquerableSpots) {
        int width = grid.getWidth();
        int height = grid.getHeight();

        // Ensure that no two spots have the same coordinates
        Set<String> occupiedSpots = new HashSet<>();

        for (int i = 0; i < numConquerableSpots; i++) {
            int xCoordinate = generateRandomCoordinate(width);
            int yCoordinate = generateRandomCoordinate(height);

            // Generate a unique key for this spot
            String spotKey = xCoordinate + "," + yCoordinate;

            // If the spot is already occupied, generate new coordinates until it's unique
            while (occupiedSpots.contains(spotKey)) {
                xCoordinate = generateRandomCoordinate(width);
                yCoordinate = generateRandomCoordinate(height);
                spotKey = xCoordinate + "," + yCoordinate;
            }

            ConquerableSpot conquerableSpot = new ConquerableSpot();
            conquerableSpot.setxCoordinate(xCoordinate);
            conquerableSpot.setyCoordinate(yCoordinate);
            grid.getConquerableSpots().add(conquerableSpot);

            // Mark the spot as occupied
            occupiedSpots.add(spotKey);
        }
    }
    private int generateRandomCoordinate(int size) {
        // Generate a random number between -size and size (inclusive)
        return (int) (Math.random() * (2 * size + 1)) - size;
    }

    public Grid getGrid() {
        return gridRepository.findFirstByOrderByIdAsc();
    }

    public void saveGrid(Grid grid) {
        gridRepository.save(grid);
    }



    public List<ConquerableSpot> getConquerableSpots() {
        Grid grid = getGrid();
        return grid.getConquerableSpots();
    }

    private boolean isBarbarianVillageOccupyingSpot(Grid grid, int xCoordinate, int yCoordinate) {
        for (BarbarianVillage barbarianVillage : grid.getBarbarianVillages()) {
            if (barbarianVillage.getXCoordinate() == xCoordinate && barbarianVillage.getYCoordinate() == yCoordinate) {
                return true;
            }
        }
        return false;
    }

    public void purgeEntireCity() {
        gridRepository.deleteAll();
    }

    public void addConquerableSpot(int xCoordinate, int yCoordinate) {
        Grid grid = getGrid(); // Get the grid from the database
        if (grid != null) {
            ConquerableSpot conquerableSpot = new ConquerableSpot();
            conquerableSpot.setxCoordinate(xCoordinate);
            conquerableSpot.setyCoordinate(yCoordinate);
            grid.getConquerableSpots().add(conquerableSpot);
            gridRepository.save(grid); // Save the updated grid with the new conquerable spot
        }
    }

}

