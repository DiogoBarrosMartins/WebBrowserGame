package com.superapi.gamerealm.dto.troops;

import com.superapi.gamerealm.model.troop.TroopType;

public class TrainTroopsRequest {
    private TroopType troopType;
    private Integer quantity;

    public TrainTroopsRequest(TroopType troopType, Integer quantity) {
        this.troopType = troopType;
        this.quantity = quantity;
    }

    public TrainTroopsRequest() {
    }

// getters and setters

    public TroopType getTroopType() {
        return troopType;
    }

    public void setTroopType(TroopType troopType) {
        this.troopType = troopType;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}

