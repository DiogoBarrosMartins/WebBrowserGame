package com.superapi.gamerealm.dto;

import com.superapi.gamerealm.component.Coordinates;
import com.superapi.gamerealm.model.Village;
import com.superapi.gamerealm.model.resources.TypeOfResource;
import com.superapi.gamerealm.model.resources.Resources;

import java.util.List;

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

    private static void mapResourcesToDTO(List<Resources> resources, VillageDTO villageDTO) {
        for (Resources resource : resources) {
            switch (resource.getType()) {
                case WHEAT:
                    villageDTO.setWheat(resource.getAmount());
                    break;
                case GOLD:
                    villageDTO.setGold(resource.getAmount());
                    break;
                case WOOD:
                    villageDTO.setWood(resource.getAmount());
                    break;
                case STONE:
                    villageDTO.setStone(resource.getAmount());
                    break;
                default:
                    // Do nothing
            }
        }
    }

    public static Village toEntity(VillageDTO villageDTO) {
        Village village = new Village();
        village.setId(villageDTO.getId());

        // Instead of creating a new Coordinates object, use the existing one from the Village entity
        Coordinates coordinates = village.getCoordinates();
        if (coordinates == null) {
            coordinates = new Coordinates();
            village.setCoordinates(coordinates);
        }
        coordinates.setX(villageDTO.getX());
        coordinates.setY(villageDTO.getY());

        village.setName(villageDTO.getName());

        // Create a new Resources object for each resource type and set its amount
        Resources wheat = new Resources(TypeOfResource.WHEAT, villageDTO.getWheat());
        Resources gold = new Resources(TypeOfResource.GOLD, villageDTO.getGold());
        Resources wood = new Resources(TypeOfResource.WOOD, villageDTO.getWood());
        Resources stone = new Resources(TypeOfResource.STONE, villageDTO.getStone());

        // Associate the Resources objects with the Village
        wheat.setVillage(village);
        gold.setVillage(village);
        wood.setVillage(village);
        stone.setVillage(village);

        // Add the Resources objects to the Village's list of resources
        village.getResources().add(wheat);
        village.getResources().add(gold);
        village.getResources().add(wood);
        village.getResources().add(stone);

        // Note: accountId should be set through the Account entity using a service or repository.
        // village.setAccount(/* Account entity */);
        village.setLastUpdated(villageDTO.getLastUpdated());
        return village;
    }


}

