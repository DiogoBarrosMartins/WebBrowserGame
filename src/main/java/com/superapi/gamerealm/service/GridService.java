package com.superapi.gamerealm.service;


import com.superapi.gamerealm.model.BarbarianVillage;
import com.superapi.gamerealm.model.ConquerableSpot;
import com.superapi.gamerealm.model.Grid;
import com.superapi.gamerealm.model.Village;
import com.superapi.gamerealm.repository.GridRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GridService  {

    private final GridRepository gridRepository;

    @Autowired
    public GridService(GridRepository gridRepository) {
        this.gridRepository = gridRepository;
    }

    public Grid createAndInitializeGrid(int width, int height, int numBarbarianVillages) {
        Grid grid = new Grid(width, height);
        for (int i = 0; i < numBarbarianVillages; i++) {
            int xCoordinate = generateRandomCoordinate(width);
            int yCoordinate = generateRandomCoordinate(height);
            placeBarbarianVillageOnGrid(grid, xCoordinate, yCoordinate); // Pass the grid object to update
        }
        placeConquerableSpotsOnGrid(grid, numBarbarianVillages);
        gridRepository.save(grid); // Save the grid to the database
        return grid;
    }

    public void placeVillageOnGrid(Grid grid, int xCoordinate, int yCoordinate) {
        Village village = new Village();
        village.setXCoordinate(xCoordinate);
        village.setYCoordinate(yCoordinate);
        grid.getVillages().add(village);
        gridRepository.save(grid);
    }

    public void placeBarbarianVillageOnGrid(Grid grid, int xCoordinate, int yCoordinate) {
        BarbarianVillage barbarianVillage = new BarbarianVillage();
        barbarianVillage.setXCoordinate(xCoordinate);
        barbarianVillage.setYCoordinate(yCoordinate);
        grid.getBarbarianVillages().add(barbarianVillage);
        gridRepository.save(grid);
    }
    public void replaceConquerableSpotWithVillage(Grid grid, int xCoordinate, int yCoordinate) {
        ConquerableSpot conquerableSpotToRemove = grid.getConquerableSpots()
                .stream()
                .filter(spot -> spot.getxCoordinate() == xCoordinate && spot.getyCoordinate() == yCoordinate)
                .findFirst()
                .orElse(null);

        if (conquerableSpotToRemove != null) {
            grid.getConquerableSpots().remove(conquerableSpotToRemove);
            placeVillageOnGrid(grid, xCoordinate, yCoordinate);
            gridRepository.save(grid);
        }
    }
    public Grid getGrid() {
        Grid grid = gridRepository.findFirstByOrderByIdAsc();
        if (grid == null) {
            // If no grid exists, create a new one with default size (10x10)
            grid = createAndInitializeGrid(10, 10, 5);
        }
        return grid;
    }
    public void saveGrid(Grid grid) {
        gridRepository.save(grid);
    }

    private void placeBarbarianVillagesOnGrid(Grid grid, int numBarbarianVillages) {
        int width = grid.getWidth();
        int height = grid.getHeight();
        for (int i = 0; i < numBarbarianVillages; i++) {
            int xCoordinate = generateRandomCoordinate(width);
            int yCoordinate = generateRandomCoordinate(height);
            placeBarbarianVillageOnGrid(grid, xCoordinate, yCoordinate);
        }
    }

    private void placeConquerableSpotsOnGrid(Grid grid, int numBarbarianVillages) {
        int width = grid.getWidth();
        int height = grid.getHeight();
        int totalSpots = width * height;
        int remainingSpots = totalSpots - numBarbarianVillages;
        int conquerableSpotsPlaced = 0;

        while (conquerableSpotsPlaced < remainingSpots) {
            int xCoordinate = generateRandomCoordinate(width);
            int yCoordinate = generateRandomCoordinate(height);
            if (!isBarbarianVillageOccupyingSpot(grid, xCoordinate, yCoordinate)) {
                ConquerableSpot conquerableSpot = new ConquerableSpot();
                conquerableSpot.setxCoordinate(xCoordinate);
                conquerableSpot.setyCoordinate(yCoordinate);
                grid.getConquerableSpots().add(conquerableSpot);
                gridRepository.save(grid);
                conquerableSpotsPlaced++;
            }
        }
    }

    private int generateRandomCoordinate(int size) {
        return (int) (Math.random() * size);
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
}

