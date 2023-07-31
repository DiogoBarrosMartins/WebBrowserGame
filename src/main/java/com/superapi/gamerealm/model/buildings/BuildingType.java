package com.superapi.gamerealm.model.buildings;

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
}
