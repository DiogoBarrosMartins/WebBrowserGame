package com.superapi.gamerealm.dto;

import java.time.LocalDateTime;

public class TroopTrainingQueueDTO {
    private Long id;
    private String troopTypeName;
    private LocalDateTime trainingEndTime;

    // Constructors
    public TroopTrainingQueueDTO(Long id, String troopTypeName, LocalDateTime trainingEndTime) {
        this.id = id;
        this.troopTypeName = troopTypeName;
        this.trainingEndTime = trainingEndTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTroopTypeName() {
        return troopTypeName;
    }

    public void setTroopTypeName(String troopTypeName) {
        this.troopTypeName = troopTypeName;
    }

    public LocalDateTime getTrainingEndTime() {
        return trainingEndTime;
    }

    public void setTrainingEndTime(LocalDateTime trainingEndTime) {
        this.trainingEndTime = trainingEndTime;
    }
// Getters and setters
    // ...
}
