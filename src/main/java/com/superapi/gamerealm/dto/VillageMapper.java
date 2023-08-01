package com.superapi.gamerealm.dto;

import com.superapi.gamerealm.component.Coordinates;
import com.superapi.gamerealm.model.Village;
import com.superapi.gamerealm.model.resources.TypeOfResource;
import com.superapi.gamerealm.model.resources.Resources;
import org.springframework.stereotype.Component;
public class VillageMapper {
    public static VillageDTO toDTO(Village village) {
        VillageDTO villageDTO = new VillageDTO();
        villageDTO.setId(village.getId());
        villageDTO.setX(village.getCoordinates().getX());
        villageDTO.setY(village.getCoordinates().getY());
        villageDTO.setName(village.getName());
        villageDTO.setAccountId(village.getAccount().getId());
        villageDTO.setLastUpdated(village.getLastUpdated());

        // Map resource fields
        mapResourcesToDTO(village.getResources(), villageDTO);

        return villageDTO;
    }

    private static void mapResourcesToDTO(Resources resources, VillageDTO villageDTO) {
        villageDTO.setWheat(resources.getAmount(TypeOfResource.WHEAT));
        villageDTO.setGold(resources.getAmount(TypeOfResource.GOLD));
        villageDTO.setWood(resources.getAmount(TypeOfResource.WOOD));
        villageDTO.setStone(resources.getAmount(TypeOfResource.STONE));
    }

    public static Village toEntity(VillageDTO villageDTO) {
        Village village = new Village();
        village.setId(villageDTO.getId());

        // Instead of creating a new Coordinates object, use the existing one from the Village entity
        Coordinates coordinates = village.getCoordinates();
        coordinates.setX(villageDTO.getX());
        coordinates.setY(villageDTO.getY());

        village.setName(villageDTO.getName());

        // Set the existing Resources object if it exists, or create a new one if it doesn't
        Resources resources = village.getResources();
        if (resources == null) {
            resources = new Resources();
        }
        resources.setAmount(TypeOfResource.WHEAT, villageDTO.getWheat());
        resources.setAmount(TypeOfResource.GOLD, villageDTO.getGold());
        resources.setAmount(TypeOfResource.WOOD, villageDTO.getWood());
        resources.setAmount(TypeOfResource.STONE, villageDTO.getStone());

        village.setResources(resources);

        // Note: accountId should be set through the Account entity using a service or repository.
        // village.setAccount(/* Account entity */);
        village.setLastUpdated(villageDTO.getLastUpdated());
        return village;
    }

}

