package com.superapi.gamerealm.dto;

import com.superapi.gamerealm.model.Village;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
@Mapper(componentModel = "spring", uses = {ResourcesMapper.class, ConstructionMapper.class, VillageTroopsMapper.class, TroopTrainingQueueMapper.class})
public interface VillageMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "x", target = "x")
    @Mapping(source = "y", target = "y")
    @Mapping(source = "lastUpdated", target = "lastUpdated")
    @Mapping(source = "account.id", target = "accountId")
    @Mapping(source = "resources", target = "resourcesDTO")
    @Mapping(source = "constructions", target = "constructionDTOS")
    @Mapping(source = "troopQueue", target = "troopTrainingQueueDTOS")
    @Mapping(source = "troops", target = "villageTroopDTOS")
    VillageDTO villageToVillageDTO(Village village);

    @InheritInverseConfiguration
    Village villageDTOToVillage(VillageDTO villageDTO);
}