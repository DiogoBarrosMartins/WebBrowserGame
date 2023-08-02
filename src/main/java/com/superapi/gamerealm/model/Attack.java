package com.superapi.gamerealm.model;

import com.superapi.gamerealm.model.troop.Troop;

import java.util.List;

public class Attack {
    private Village attackerVillage;
    private Village defenderVillage;
    private List<Troop> troops;

    public Attack() {
    }

    public Attack(Village attackerVillage, Village defenderVillage, List<Troop> troops) {
        this.attackerVillage = attackerVillage;
        this.defenderVillage = defenderVillage;
        this.troops = troops;
    }

    public Village getAttackerVillage() {
        return attackerVillage;
    }

    public void setAttackerVillage(Village attackerVillage) {
        this.attackerVillage = attackerVillage;
    }

    public Village getDefenderVillage() {
        return defenderVillage;
    }

    public void setDefenderVillage(Village defenderVillage) {
        this.defenderVillage = defenderVillage;
    }

    public List<Troop> getTroops() {
        return troops;
    }

    public void setTroops(List<Troop> troops) {
        this.troops = troops;
    }
}