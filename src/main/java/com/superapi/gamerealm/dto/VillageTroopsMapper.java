package com.superapi.gamerealm.dto;

import com.superapi.gamerealm.model.troop.VillageTroops;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VillageTroopsMapper {
    @Mapping(source = "troopType.name", target = "troopTypeName")
    VillageTroopsDTO villageTroopsToDTO(VillageTroops villageTroops);

    // ... reverse mapping and other methods as needed ...
}
