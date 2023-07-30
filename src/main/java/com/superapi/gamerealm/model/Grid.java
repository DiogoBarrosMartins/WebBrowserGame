package com.superapi.gamerealm.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Grid {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int width;
    private int height;


    public Grid(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public Grid() {
    }




}
