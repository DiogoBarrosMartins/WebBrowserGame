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
import com.superapi.gamerealm.model.resources.TypeOfResource;
import com.superapi.gamerealm.model.troop.Troop;
import com.superapi.gamerealm.repository.ResourcesRepository;
import com.superapi.gamerealm.repository.VillageRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Time;
import java.util.ArrayList;
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

    @Autowired
    public VillageService(VillageRepository villageRepository, GridService gridService, ModelMapper modelMapper, ResourceService resourceService, CombatService combatService) {
        this.villageRepository = villageRepository;
        this.gridService = gridService;
        this.modelMapper = modelMapper;
        this.resourceService = resourceService;
        this.combatService = combatService;
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
        Resources wheat = new Resources(TypeOfResource.WHEAT, BigDecimal.valueOf(1000L));
        Resources gold = new Resources(TypeOfResource.GOLD, BigDecimal.valueOf(500L));
        Resources wood = new Resources(TypeOfResource.WOOD, BigDecimal.valueOf(1000L));
        Resources stone = new Resources(TypeOfResource.STONE, BigDecimal.valueOf(500L));

        wheat.setVillage(village);
        gold.setVillage(village);
        wood.setVillage(village);
        stone.setVillage(village);

        village.getResources().add(wheat);
        village.getResources().add(gold);
        village.getResources().add(wood);
        village.getResources().add(stone);
    }

    public VillageDTO createVillage(VillageDTO villageDTO) {
        Village village = modelMapper.map(villageDTO, Village.class);
        Village createdVillage = villageRepository.save(village);
        return modelMapper.map(createdVillage, VillageDTO.class);
    }

    public List<VillageDTO> getAllVillages() {
        List<Village> villages = villageRepository.findAll();
        return villages.stream()
                .map(VillageMapper::toDTO)
                .collect(Collectors.toList());
    }

    public VillageDTO getVillageByAccountUsername(String username) {
        List<Village> villages = villageRepository.findByAccountUsername(username);

        if (villages.isEmpty()) {
            throw new RuntimeException("Village not found for account username: " + username);
        }

        Village village = villages.get(0);
        resourceService.updateResourcesAndLastUpdated(village);

        return VillageMapper.toDTO(village);
    }

    public VillageDTO getVillageById(Long villageId) {
        Village village = villageRepository.findById(villageId)
                .orElseThrow(() -> new RuntimeException("Village not found with ID: " + villageId));
        return VillageMapper.toDTO(village);
    }

    public List<VillageDTO> findAllVillagesByAccountId(Long accountId) {
        List<Village> villages = villageRepository.findAllByAccountId(accountId);
        return villages.stream()
                .map(VillageMapper::toDTO)
                .collect(Collectors.toList());
    }
    public Village getVillage(Long id) {
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


    /**
     public void handleAttack(Attack attack) {
     Village attackerVillage = attack.getAttackerVillage();
     Village defenderVillage = attack.getDefenderVillage();

     Time travelTime = calculateTravelTime(attackerVillage.getCoordinates(), defenderVillage.getCoordinates());

     List<Troop> attackingTroops = attack.getTroops();
     List<Troop> defendingTroops = defenderVillage.getTroops();

     combatService.attack(attackingTroops, defendingTroops);

     int attackingTroopsCount = getTotalTroopsCount(attackingTroops);
     int defendingTroopsCount = getTotalTroopsCount(defendingTroops);

     // Calculate the resources gained/lost from the attack
     Resources loot = calculateLoot(attackingTroopsCount, defendingTroopsCount, defenderVillage.getResources());
     Resources casualties = calculateCasualties(attackingTroopsCount, defendingTroopsCount);

     // Update the attacker's and defender's villages
     attackerVillage.setLastAttack(new Date());
     attackerVillage.setResources(addResources(attackerVillage.getResources(), loot));
     attackerVillage.setTroops(subtractTroops(attackerVillage.getTroops(), attack.getTroops()));

     defenderVillage.setResources(subtractResources(defenderVillage.getResources(), loot));
     defenderVillage.setTroops(subtractTroops(defenderVillage.getTroops(), defendingTroops));
     defenderVillage.setIsUnderAttack(false);

     // Schedule the return of the attacking troops
     Time returnTime = travelTime.add(travelTime);
     scheduleReturn(attackerVillage, attackingTroops, returnTime);
     }

     private Time calculateTravelTime(Coordinates source, Coordinates destination) {
     int distance = calculateDistance(source, destination);
     int travelTimeInSeconds = distance * 60 * 60 / TROOPS_SPEED;
     return Time.valueOf(LocalTime.ofSecondOfDay(travelTimeInSeconds));
     }




     public void handleAttack(Attack attack) {
     Village attackerVillage = attack.getAttackerVillage();
     Village defenderVillage = attack.getDefenderVillage();

     int distance = calculateDistance(attackerVillage.getCoordinates(), defenderVillage.getCoordinates());
     int travelTime = calculateTravelTime(distance);

     List<Troop> attackingTroops = attack.getTroops();
     List<Troop> defendingTroops = defenderVillage.getTroops();

     combatService.attack(attackingTroops, defendingTroops);

     int attackingTroopsCount = getTotalTroopsCount(attackingTroops);
     int defendingTroopsCount = getTotalTroopsCount(defendingTroops);

     // Calculate the resources gained/lost from the attack
     Resources loot = calculateLoot(attackingTroopsCount, defendingTroopsCount, defenderVillage.getResources());
     Resources casualties = calculateCasualties(attackingTroopsCount, defendingTroopsCount);

     // Update the attacker's and defender's villages
     attackerVillage.setLastAttack(new Date());
     attackerVillage.setResources(addResources(attackerVillage.getResources(), loot));
     attackerVillage.setTroops(subtractTroops(attackerVillage.getTroops(), attack.getTroops()));

     defenderVillage.setResources(subtractResources(defenderVillage.getResources(), loot));
     defenderVillage.setTroops(subtractTroops(defenderVillage.getTroops(), defendingTroops));
     defenderVillage.setCasualties(addResources(defenderVillage.getCasualties(), casualties));
     defenderVillage.setIsUnderAttack(false);

     saveVillage(attackerVillage);
     saveVillage(defenderVillage);
     }

     // other methods


     //todo
     public Resources calculateLoot(int attackingTroopsCount, int defendingTroopsCount, Resources defenderResources) {
     int totalDefenderResources = resourceService.getTotalResources(defenderResources);
     double lootPercentage = (double) attackingTroopsCount / (attackingTroopsCount + defendingTroopsCount);
     int lootAmount = (int) Math.round(totalDefenderResources * lootPercentage);
     return resourceService.createResources(lootAmount);
     }
     //todo
     public Resources calculateCasualties(int attackingTroopsCount, int defendingTroopsCount) {
     double casualtiesPercentage = 1 - (double) defendingTroopsCount / attackingTroopsCount;
     int casualtiesAmount = (int) Math.round(attackingTroopsCount * casualtiesPercentage);
     return resourceService.createResources(casualtiesAmount);
     }

     public Resources addResources(Resources r1, Resources r2) {

     Resources gold;
     gold.setAmount(r1.getAmount(TypeOfResource.GOLD) + r2.getAmount(TypeOfResource.GOLD);
     int food = r1.getFood() + r2.getFood();
     int wood = r1.getWood() + r2.getWood();
     return resourceService.createResource(gold, wheat, stone,  wood);
     }

     public List<Troop> subtractTroops(List<Troop> troops1, List<Troop> troops2) {
     List<Troop> result = new ArrayList<>();
     for (int i = 0; i < troops1.size(); i++) {
     Troop t1 = troops1.get(i);
     Troop t2 = troops2.get(i);
     result.add(new Troop(t1.getType(), t1.getCount() - t2.getCount()));
     }
     return result;
     }

     **/

}