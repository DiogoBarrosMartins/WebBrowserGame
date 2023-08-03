package com.superapi.gamerealm.dto;

import com.superapi.gamerealm.model.resources.Resources;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ResourcesMapper {

    @Mapping(source = "wood", target = "wood")
    @Mapping(source = "wheat", target = "wheat")
    @Mapping(source = "stone", target = "stone")
    @Mapping(source = "gold", target = "gold")
    ResourcesDTO resourcesToResourcesDTO(Resources resources);

    @InheritInverseConfiguration
    Resources resourcesDtoToResources(ResourcesDTO resourcesDTO);


    List<ResourcesDTO> resourcesListToResourcesDTOList(List<Resources> resources);

    List<Resources> resourcesDTOListToResourcesList(List<ResourcesDTO> resourcesDTOs);
}
