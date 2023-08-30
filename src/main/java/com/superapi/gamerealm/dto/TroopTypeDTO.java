package com.superapi.gamerealm.dto;

import com.superapi.gamerealm.model.resources.TypeOfResource;

import java.util.Map;

public class TroopTypeDTO {
    private String name;
    private Map<TypeOfResource, Double> resourcesRequired;
    private int trainingTime;

    // Constructors
    public TroopTypeDTO(String name, Map<TypeOfResource, Double> resourcesRequired, int trainingTime) {
        this.name = name;
        this.resourcesRequired = resourcesRequired;
        this.trainingTime = trainingTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<TypeOfResource, Double> getResourcesRequired() {
        return resourcesRequired;
    }

    public void setResourcesRequired(Map<TypeOfResource, Double> resourcesRequired) {
        this.resourcesRequired = resourcesRequired;
    }

    public int getTrainingTime() {
        return trainingTime;
    }

    public void setTrainingTime(int trainingTime) {
        this.trainingTime = trainingTime;
    }
// Getters and setters
    // ...
}
