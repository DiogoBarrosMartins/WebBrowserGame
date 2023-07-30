package com.superapi.gamerealm.component;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Coordinates {
    @Column(nullable = false)
    private int x;

    @Column(nullable = false)
    private int y;

    public Coordinates() {
    }

    public Coordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
