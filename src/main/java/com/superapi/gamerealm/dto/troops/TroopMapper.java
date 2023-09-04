package com.superapi.gamerealm.dto.troops;

import com.superapi.gamerealm.dto.TroopTrainingQueueDTO;
import com.superapi.gamerealm.model.troop.TroopTrainingQueue;
import com.superapi.gamerealm.model.troop.TroopType;

public class TroopMapper {

    public static TroopTrainingQueueDTO toDTO(TroopTrainingQueue entity) {
        return new TroopTrainingQueueDTO(
                entity.getId(),
                entity.getTroopType().getName(),
                entity.getTrainingEndTime()
        );
    }

    public static TroopTypeDTO toDTO(TroopType entity) {
        return new TroopTypeDTO(
                entity.getName(),
                entity.getHealth(),
                entity.getArmor(),
                entity.getAttack(),
                entity.getCarryCapacity(),
                entity.getTrainingTime(),
                entity.getResourcesRequired(),
                entity.getDescription()
        );
    }

    // Add other mappings as necessary
}
