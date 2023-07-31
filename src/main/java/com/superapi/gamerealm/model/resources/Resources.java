package com.superapi.gamerealm.model.resources;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class Resources {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private BigDecimal wheat;

    @Column(nullable = false)
    private BigDecimal gold;

    @Column(nullable = false)
    private BigDecimal wood;

    @Column(nullable = false)
    private BigDecimal stone;

    public Resources() {
        this.wood = BigDecimal.ZERO;
        this.wheat = BigDecimal.ZERO;
        this.stone = BigDecimal.ZERO;
        this.gold = BigDecimal.ZERO;
    }
    public BigDecimal getWheat() {
        return  wheat;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setWheat(BigDecimal wheat) {
        this.wheat = wheat;
    }

    public BigDecimal getGold() {
        return gold;
    }

    public void setGold(BigDecimal gold) {
        this.gold = gold;
    }

    public BigDecimal getWood() {
        return wood;
    }

    public void setWood(BigDecimal wood) {
        this.wood = wood;
    }

    public BigDecimal getStone() {
        return stone;
    }

    public void setStone(BigDecimal stone) {
        this.stone = stone;
    }
// Constructors, getters, and setters
}

