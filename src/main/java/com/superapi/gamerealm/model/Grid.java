package com.superapi.gamerealm.model;

import com.superapi.gamerealm.component.Coordinates;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Entity
public class Grid {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int width;
    private int height;
    @ElementCollection
    private List<Coordinates> villageCoordinates = new ArrayList<>();

    public Grid(int width, int height){
        this.width = width;
        this.height = height;
    }

    public Grid() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public List<Coordinates> getVillageCoordinates() {
        return villageCoordinates;
    }

    public void setVillageCoordinates(List<Coordinates> villageCoordinates) {
        this.villageCoordinates = villageCoordinates;
    }
}




