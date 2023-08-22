package com.superapi.gamerealm.dto;


import com.superapi.gamerealm.model.buildings.Construction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ConstructionMapper {

    @Mapping(source = "building.id", target = "buildingId")
    ConstructionDTO constructionToConstructionDTO(Construction construction);

    @Mapping(source = "buildingId", target = "building.id")
    Construction constructionDTOToConstruction(ConstructionDTO constructionDTO);
}
