package com.superapi.gamerealm.dto;

import com.superapi.gamerealm.model.troop.TroopTrainingQueue;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TroopTrainingQueueMapper {
    @Mapping(source = "troopType.name", target = "troopTypeName")
    TroopTrainingQueueDTO troopTrainingQueueToDTO(TroopTrainingQueue troopTrainingQueue);

    // ... reverse mapping and other methods as needed ...
}

