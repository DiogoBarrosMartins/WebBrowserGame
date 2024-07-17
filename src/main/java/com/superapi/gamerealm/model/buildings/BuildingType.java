package com.superapi.gamerealm.model.buildings;

public enum BuildingType {
    FOREST,
    MINE,
    QUARRY,
    FARM,
    BARRACKS,
    ARCHERY_RANGE,
    STABLES,
    WALLS;


    public boolean isResourceBuilding() {
        return this == FOREST || this == MINE || this == QUARRY || this == FARM;
    }



}
