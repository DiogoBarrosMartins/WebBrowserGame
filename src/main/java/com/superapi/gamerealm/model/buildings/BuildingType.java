package com.superapi.gamerealm.model.buildings;

import com.superapi.gamerealm.model.resources.TypeOfResource;

public enum BuildingType {
    FARM("Farm"),
    QUARRY("Quarry"),
    MINE("Mine"),
    FOREST("Forest"),
    PUB("Pub"),
    BARRACKS("Barracks"),
    GRAIN_SILO("Grain Silo"),
    STORAGE("Storage"),
    RESEARCH_CENTER("Research Center"),
    STABLE("Stable"),
    SIEGE_WORKSHOP("Siege Workshop");

    private final String displayName;

    BuildingType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }




    public TypeOfResource getResourceName() {

        switch (this) {
            case FARM:
                return TypeOfResource.WHEAT;
            case FOREST:
                return TypeOfResource.WOOD;
            case QUARRY:
                return TypeOfResource.STONE;
            case MINE:
                return TypeOfResource.GOLD;
            default:
                throw new IllegalArgumentException("Unsupported building type: " + this);
        }
    }


}
