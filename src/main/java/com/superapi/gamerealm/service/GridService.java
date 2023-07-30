package com.superapi.gamerealm.service;


import com.superapi.gamerealm.model.Grid;
import com.superapi.gamerealm.repository.GridRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GridService {

    private final GridRepository gridRepository;

    @Autowired
    public GridService(GridRepository gridRepository) {
        this.gridRepository = gridRepository;
    }

    public Grid createAndInitializeGrid() {


        Grid existingGrid = gridRepository.findFirstByOrderByIdAsc();

        if (existingGrid != null) {
            // If a grid exists, return it instead of creating a new one
            return existingGrid;
        }

        Grid grid = new Grid(5, 5); // Create a 5x5 grid

        // Add 5 barbarian villages to the grid at random positions
        addRandomBarbarianVillages(grid, 5);

        // Add 20 conquerable spots to the grid
        addConquerableSpots(grid, 20);

        gridRepository.save(grid); // Save the grid to the database
        return grid;
    }

    private void addRandomBarbarianVillages(Grid grid, int numBarbarianVillages) {

    }

    private void addConquerableSpots(Grid grid, int numConquerableSpots) {

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


    public void purgeEntireCity() {
        gridRepository.deleteAll();
    }

    public void addConquerableSpot() {

    }

}

