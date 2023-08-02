package com.superapi.gamerealm.dto;

import com.superapi.gamerealm.model.buildings.BuildingType;

public class NonResourceBuildingDTO {
    private Long id;
    private BuildingType type;
    private int level;
    private int maxLevel;
    private int[] resourcesNeeded;
    private long timeLeft;

    public NonResourceBuildingDTO(Long id, BuildingType type, int level, int maxLevel, int[] nonResourceBuildingResourcesNeeded) {
    }

    public NonResourceBuildingDTO() {
    }

    public NonResourceBuildingDTO(Long id, BuildingType type, int level, int maxLevel, int[] resourcesNeeded, long timeLeft) {
        this.id = id;
        this.type = type;
        this.level = level;
        this.maxLevel = maxLevel;
        this.resourcesNeeded = resourcesNeeded;
        this.timeLeft = timeLeft;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BuildingType getType() {
        return type;
    }

    public void setType(BuildingType type) {
        this.type = type;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public void setMaxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
    }

    public int[] getResourcesNeeded() {
        return resourcesNeeded;
    }

    public void setResourcesNeeded(int[] resourcesNeeded) {
        this.resourcesNeeded = resourcesNeeded;
    }

    public long getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(long timeLeft) {
        this.timeLeft = timeLeft;
    }
// getters and setters omitted for brevity
}
