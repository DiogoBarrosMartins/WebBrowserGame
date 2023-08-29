package com.superapi.gamerealm.model;

public class Attack {
    private Village attackerVillage;
    private Village defenderVillage;

    public Attack() {
    }

    public Attack(Village attackerVillage, Village defenderVillage ) {
        this.attackerVillage = attackerVillage;
        this.defenderVillage = defenderVillage;
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


}