package com.superapi.gamerealm.model.resources;


import com.superapi.gamerealm.model.Village;
import jakarta.persistence.*;
@Entity
public class Resources {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "village_id")
    private Village village;

    @Column(nullable = false)
    private Double wood;

    @Column(nullable = false)
    private Double wheat;

    @Column(nullable = false)
    private Double stone;

    @Column(nullable = false)
    private Double gold;

    // other fields


    public Resources() {
    }

    public Resources(Long id, Village village, Double wood, Double wheat, Double stone, Double gold) {
        this.id = id;
        this.village = village;
        this.wood = wood;
        this.wheat = wheat;
        this.stone = stone;
        this.gold = gold;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Village getVillage() {
        return village;
    }

    public void setVillage(Village village) {
        this.village = village;
    }

    public Double getWood() {
        return wood;
    }

    public void setWood(Double wood) {
        this.wood = wood;
    }

    public Double getWheat() {
        return wheat;
    }

    public void setWheat(Double wheat) {
        this.wheat = wheat;
    }

    public Double getStone() {
        return stone;
    }

    public void setStone(Double stone) {
        this.stone = stone;
    }

    public Double getGold() {
        return gold;
    }

    public void setGold(Double gold) {
        this.gold = gold;
    }
}








