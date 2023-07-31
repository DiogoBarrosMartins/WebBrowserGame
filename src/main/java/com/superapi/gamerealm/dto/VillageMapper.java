package com.superapi.gamerealm.dto;

import com.superapi.gamerealm.component.Coordinates;
import com.superapi.gamerealm.model.Village;
import org.springframework.stereotype.Component;
@Component
public class VillageMapper {

    public static VillageDTO toDTO(Village village) {
        VillageDTO villageDTO = new VillageDTO();
        villageDTO.setId(village.getId());
        villageDTO.setX(village.getCoordinates().getX());
        villageDTO.setY(village.getCoordinates().getY());
        villageDTO.setName(village.getName());
        villageDTO.setAccountId(village.getAccount().getId());
        villageDTO.setLastUpdated(village.getLastUpdated());
        return villageDTO;
    }


    public static Village toEntity(VillageDTO villageDTO) {
        Village village = new Village();
        village.setId(villageDTO.getId());

        // Instead of creating a new Coordinates object, use the existing one from the Village entity
        Coordinates coordinates = village.getCoordinates();
        coordinates.setX(villageDTO.getX());
        coordinates.setY(villageDTO.getY());

        village.setName(villageDTO.getName());
        // Note: accountId should be set through the Account entity using a service or repository.
        // village.setAccount(/* Account entity */);
        village.setLastUpdated(villageDTO.getLastUpdated());
        return village;
    }
}
