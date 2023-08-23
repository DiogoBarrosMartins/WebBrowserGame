package com.superapi.gamerealm.dto;


import com.superapi.gamerealm.model.buildings.Construction;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring" )
public interface ConstructionMapper {




    default Construction fromId(Long id) {
        if (id == null) {
            return null;
        }
        Construction construction = new Construction();
        construction.setId(id);
        return construction;
    }
}

