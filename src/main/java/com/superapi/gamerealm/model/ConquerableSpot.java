package com.superapi.gamerealm.model;
import jakarta.persistence.*;

@Entity
public class ConquerableSpot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Conquerable spot coordinates (x, y) on the grid
    @Column(nullable = false)
    private int xCoordinate;

    @Column(nullable = false)
    private int yCoordinate;

    // Other attributes of the conquerable spot (if any)
    // ...
    @ManyToOne
    @JoinColumn(name = "grid_id")
    private Grid grid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getXCoordinate() {
        return xCoordinate;
    }

    public void setxCoordinate(int xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    public int getYCoordinate() {
        return yCoordinate;
    }

    public void setyCoordinate(int yCoordinate) {
        this.yCoordinate = yCoordinate;
    }

    public Grid getGrid() {
        return grid;
    }

    public void setGrid(Grid grid) {
        this.grid = grid;
    }


}
