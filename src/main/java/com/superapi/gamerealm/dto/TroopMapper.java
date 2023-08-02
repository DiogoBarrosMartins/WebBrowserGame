package com.superapi.gamerealm.dto;

import com.superapi.gamerealm.model.troop.Troop;

public class TroopMapper {
    public static TroopDTO toDTO(Troop troop) {
        return new TroopDTO(
                troop.getId(),
                troop.getType(),
                troop.getLevel(),
                troop.getTrainingTime(),
                troop.getResourcesNeeded(),
                troop.getAttack(),
                troop.getDefense(),
                troop.getHealth()
        );
    }

    public static Troop toEntity(TroopDTO troopDTO) {
        return new Troop(
                troopDTO.getId(),
                troopDTO.getType(),
                troopDTO.getLevel(),
                troopDTO.getTrainingTime(),
                troopDTO.getResourcesNeeded(),
                troopDTO.getAttack(),
                troopDTO.getDefense(),
                troopDTO.getHealth()
        );
    }
}
