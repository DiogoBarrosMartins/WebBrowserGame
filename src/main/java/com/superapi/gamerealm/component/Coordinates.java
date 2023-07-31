package com.superapi.gamerealm.component;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class Coordinates {
    @Column(nullable = false)
    private int x;

    @Column(nullable = false)
    private int y;

    @JsonProperty("hasVillage")
    @Column(nullable = false)
    private boolean hasVillage;

    public Coordinates() {
    }


    public Coordinates(int x, int y) {
        this.x = x;
        this.y = y;
        this.hasVillage = false;
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

    public boolean hasVillage() {
        return hasVillage;
    }

    public void setHasVillage(boolean hasVillage) {
        this.hasVillage = hasVillage;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isHasVillage() {
        return hasVillage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Coordinates)) return false;
        Coordinates that = (Coordinates) o;
        return getX() == that.getX() && getY() == that.getY();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getX(), getY());
    }
}

