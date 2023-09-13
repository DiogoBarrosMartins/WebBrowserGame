package com.superapi.gamerealm.service;

import com.superapi.gamerealm.dto.VillageDTO;
import com.superapi.gamerealm.dto.VillageMapper;
import com.superapi.gamerealm.dto.building.BuildingMapper;
import com.superapi.gamerealm.dto.building.NonResourceBuildingDTO;
import com.superapi.gamerealm.dto.building.ResourceBuildingDTO;
import com.superapi.gamerealm.model.Account;
import com.superapi.gamerealm.model.Attack;
import com.superapi.gamerealm.model.Village;
import com.superapi.gamerealm.model.buildings.Building;
import com.superapi.gamerealm.model.buildings.BuildingType;
import com.superapi.gamerealm.model.resources.Resources;
import com.superapi.gamerealm.repository.VillageRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class VillageService {
    private final VillageRepository villageRepository;
    private final ModelMapper modelMapper;
    private final ResourceService resourceService;
    private final TroopTrainingService troopTrainingService;
    private final VillageMapper villageMapper;
    private final BuildingService buildingService;

    @Autowired
    public VillageService(VillageRepository villageRepository,
                          BuildingService buildingService,
                          ModelMapper modelMapper, TroopTrainingService troopTrainingService,
                          ResourceService resourceService,
                          VillageMapper villageMapper) {
        this.villageRepository = villageRepository;
        this.buildingService = buildingService;
        this.modelMapper = modelMapper;
        this.troopTrainingService = troopTrainingService;
        this.resourceService = resourceService;
        this.villageMapper = villageMapper;
    }

    public VillageDTO getVillageWithDetailsByUsername(String username) {
        Village village = villageRepository.findVillageByUsername(username).orElseThrow();
        resourceService.updateVillageResources(village);
        buildingService.processOverdueConstructions(village.getId());
        troopTrainingService.processOverdueTroopTrainings(village.getId());
        List<ResourceBuildingDTO> resourceBuildings = new ArrayList<>();
        List<NonResourceBuildingDTO> nonResourceBuildings = new ArrayList<>();

        for (Building building : village.getBuildings()) {
            if (building.isResourceBuilding()) {
                resourceBuildings.add(BuildingMapper.toResourceBuildingDTO(building));
            } else {
                nonResourceBuildings.add(BuildingMapper.toNonResourceBuildingDTO(building));
            }
        }

        VillageDTO villageDTO = villageMapper.villageToVillageDTO(village);
        villageDTO.setResourceBuildings(resourceBuildings);
        villageDTO.setNonResourceBuildings(nonResourceBuildings);
        return villageDTO;
    }


    public List<VillageDTO> getSurroundingVillages(int x, int y) {
        int radius = 24;  // Or any other desired value
        List<Village> villages = villageRepository.findVillagesInArea(x - radius, x + radius, y - radius, y + radius);

        return villages.stream()
                .map(village -> new VillageDTO(village.getId(), village.getX(), village.getY(), village.getName()))
                .collect(Collectors.toList());
    }

    public void createVillageForAccount(Account account) {

        // Declare x and y here
        int x, y;

        long villageCount = villageRepository.count();

        if (villageCount == 0) {
            x = 0;
            y = 0;
        } else {
            // Get the latest village from the database
            Village latestVillage = villageRepository.findLatestVillage(PageRequest.of(0, 1)).get(0);

            x = latestVillage.getX();
            y = latestVillage.getY();

            List<String> directions = Arrays.asList("N", "S", "E", "W", "NE", "NW", "SE", "SW");
            Collections.shuffle(directions); // Randomize the direction order

            for (String direction : directions) {
                int buffer = (Math.random() < 0.5) ? 2 : 3; // 50% chance to jump 2 or 3 squares

                switch (direction) {
                    case "N":
                        y -= buffer;
                        break;
                    case "S":
                        y += buffer;
                        break;
                    case "E":
                        x += buffer;
                        break;
                    case "W":
                        x -= buffer;
                        break;
                    case "NE":
                        x += buffer;
                        y -= buffer;
                        break;
                    case "NW":
                        x -= buffer;
                        y -= buffer;
                        break;
                    case "SE":
                        x += buffer;
                        y += buffer;
                        break;
                    case "SW":
                        x -= buffer;
                        y += buffer;
                        break;

                }

                if (villageRepository.findByXAndY(x, y) == null) {
                    break;
                }
            }
        }

        Village newVillage = new Village(x, y);
        newVillage.setAccount(account);
        initializeDefaultResources(newVillage);
        initializeDefaultBuildings(newVillage);
        villageRepository.save(newVillage);
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
        buildingService.processOverdueConstructions(village.getId());
        troopTrainingService.processOverdueTroopTrainings(village.getId());
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
        Village v = villageRepository.findById(id).orElseThrow(() -> new RuntimeException("Village not found with ID: " + id));
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

    public Village updateVillageName(String username, String newName) {
        Village village = villageRepository.findVillageByUsername(username).orElseThrow();
        village.setName(newName);
        saveVillage(village);
        return village;
    }

    public void deleteVillage(Long id) {
        Optional<Village> optionalVillage = villageRepository.findById(id);

        if (optionalVillage.isPresent()) {
            Village village = optionalVillage.get();
            villageRepository.delete(village);
        } else {
            throw new RuntimeException("No village found with ID: " + id);
        }
    }


}


