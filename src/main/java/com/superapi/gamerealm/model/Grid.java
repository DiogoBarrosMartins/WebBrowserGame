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
    @OneToMany(mappedBy = "grid", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Village> villages = new ArrayList<>();


    public Grid(int width, int height){
        this.width = width;
        this.height = height;
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

    public List<Village> getVillages() {
        return villages;
    }

    public void setVillages(List<Village> villages) {
        this.villages = villages;
    }
}




