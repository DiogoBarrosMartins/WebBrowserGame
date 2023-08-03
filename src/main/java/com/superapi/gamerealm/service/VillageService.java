package com.superapi.gamerealm.service;

import com.superapi.gamerealm.component.Coordinates;
import com.superapi.gamerealm.dto.VillageDTO;

import com.superapi.gamerealm.dto.VillageMapper;
import com.superapi.gamerealm.model.Account;
import com.superapi.gamerealm.model.Attack;
import com.superapi.gamerealm.model.Grid;
import com.superapi.gamerealm.model.Village;
import com.superapi.gamerealm.model.buildings.Building;
import com.superapi.gamerealm.model.buildings.BuildingType;
import com.superapi.gamerealm.model.resources.Resources;
import com.superapi.gamerealm.repository.VillageRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VillageService {
    private final VillageRepository villageRepository;
    private final ModelMapper modelMapper;
    private final GridService gridService;
    private final ResourceService resourceService;
    private final CombatService combatService;
    private final VillageMapper villageMapper;

    @Autowired
    public VillageService(VillageRepository villageRepository, GridService gridService, ModelMapper modelMapper, ResourceService resourceService, CombatService combatService, VillageMapper villageMapper) {
        this.villageRepository = villageRepository;
        this.gridService = gridService;
        this.modelMapper = modelMapper;
        this.resourceService = resourceService;
        this.combatService = combatService;
        this.villageMapper = villageMapper;
    }

    public Village createVillageForAccount(Account account) {
        Grid grid = gridService.getGrid();
        List<Coordinates> availableSpots = grid.getVillageCoordinates();

        if (availableSpots.isEmpty()) {
            throw new RuntimeException("No available spot for village.");
        }

        // Select a random available spot from the list
        int randomIndex = (int) (Math.random() * availableSpots.size());
        Coordinates spot = availableSpots.get(randomIndex);

        spot.setHasVillage(true); // Update the grid to indicate that a village has been created at the selected coordinate

        // Create the new Village entity with the selected coordinates
        Village newVillage = new Village(spot);
        newVillage.setAccount(account);
        initializeDefaultResources(newVillage);
        initializeDefaultBuildings(newVillage);

        villageRepository.save(newVillage);

        // Remove the selected spot from the list to avoid duplicates
        availableSpots.remove(randomIndex);

        return newVillage;
    }

    private void initializeDefaultBuildings(Village village) {
        List<Building> buildings = new ArrayList<>();

        for (BuildingType type : BuildingType.values()) {
            int numberOfBuildings = (type.ordinal() < 4) ? 4 : 1;
            for (int i = 0; i < numberOfBuildings; i++) {
                Building building = new Building(type, village);
                buildings.add(building);
            }
        }

        village.setBuildings(buildings);
    }

    private void initializeDefaultResources(Village village) {
        Resources resources = new Resources();
        resources.setWood(1000.0);
        resources.setWheat(1000.0);
        resources.setStone(500.0);
        resources.setGold(500.0);
        resources.setVillage(village);

        List<Resources> resourcesList = new ArrayList<>();
        resourcesList.add(resources);

        village.setResources(resourcesList);
    }



    public VillageDTO createVillage(VillageDTO villageDTO) {
        Village village = modelMapper.map(villageDTO, Village.class);
        Village createdVillage = villageRepository.save(village);
        return villageMapper.villageToVillageDTO(createdVillage);
    }

    public List<VillageDTO> getAllVillages() {
        List<Village> villages = villageRepository.findAll();

        return villages.stream()
                .map(villageMapper::villageToVillageDTO)
                .collect(Collectors.toList());
    }


    public VillageDTO getVillageByAccountUsername(String username) {
        List<Village> villages = villageRepository.findByAccountUsername(username);

        if (villages.isEmpty()) {
            throw new RuntimeException("Village not found for account username: " + username);
        }

        Village village = villages.get(0);

        resourceService.updateVillageResources(village);

        return villageMapper.villageToVillageDTO(village);
    }


    public VillageDTO getVillageById(Long villageId) {
        Village village = villageRepository.findById(villageId)
                .orElseThrow(() -> new RuntimeException("Village not found with ID: " + villageId));
        resourceService.updateVillageResources(village);


        return villageMapper.villageToVillageDTO(village);
    }

    public List<VillageDTO> findAllVillagesByAccountId(Long accountId) {
        List<Village> villages = villageRepository.findAllByAccountId(accountId);
        return villages.stream()
                .map(villageMapper::villageToVillageDTO)
                .collect(Collectors.toList());
    }

    public Village getVillage(Long id) {
        Village v =  villageRepository.findById(id).orElseThrow(() -> new RuntimeException("Village not found with ID: " + id));
        resourceService.updateVillageResources(v);
        return villageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Village not found"));
    }

    public void declareAttack(Attack attack) {
        Village attackerVillage = attack.getAttackerVillage();
        attackerVillage.setIsUnderAttack(true);
        saveVillage(attackerVillage);
    }

    private void saveVillage(Village village) {
        villageRepository.save(village);
    }

    public void deleteVillage(Long id) {
        Optional<Village> optionalVillage = villageRepository.findById(id);

        if (optionalVillage.isPresent()) {
            Village village = optionalVillage.get();
            Coordinates coordinates = village.getCoordinates();
            coordinates.setHasVillage(false); // Update the grid to indicate that the spot is now available
            villageRepository.delete(village);
        } else {
            throw new RuntimeException("No village found with ID: " + id);
        }
    }


}