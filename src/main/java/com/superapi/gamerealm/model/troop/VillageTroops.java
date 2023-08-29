package com.superapi.gamerealm.model.troop;

import com.superapi.gamerealm.model.Village;
import jakarta.persistence.*;

@Entity
@Table(name = "VillageTroops")
public class VillageTroops {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "villageId")
    private Village village;

    @Enumerated(EnumType.STRING)
    private TroopType troopType;

    private int count;


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

    public TroopType getTroopType() {
        return troopType;
    }

    public void setTroopType(TroopType troopType) {
        this.troopType = troopType;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
