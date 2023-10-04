package com.superapi.gamerealm.service;

import com.superapi.gamerealm.model.Village;
import com.superapi.gamerealm.model.resources.TypeOfResource;
import com.superapi.gamerealm.model.troop.TroopType;
import com.superapi.gamerealm.model.troop.VillageTroops;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;



    /**
     * the attacking damage should be spread by all the defending troops, and troops should be selected to take the damage at random
     *  if the attackers do x damage we should go to the defending troops, and one troop at random should take the hit. if that hit kills it
     *  then another defending troop at random should be selected and continue to take the damage, and so on until all damage was taken by
     *  defending troops, or, no defending troops are left. Once damage is done to a troop, it's reduced by the number of that troop's health.
     *  this approach also needs to take in consideration the armour of any given troop.
     *  if 10 damage were to be dealt, for example, but the opposite troop has 2 armour. 8 damage will be taken, but 10 damage will be considered given.
     *  the armour of the troop that takes damage should be considered individually, given the value of the troop that took the damage
     *  The combat damage should be dealt in the order described below

     *
     * this should be the damage order
     * on any given attack the combat would be processed by rounds
     * attacking siege
     * defending siege
     * defending archers
     * atacking chavalry
     * defending chavalry
     * attacking archers
     * attacking foot troops
     * defending foot troops

**/





@Service
public class CombatService {


        private final ResourceService resourceService;

    public CombatService(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    public void basicAttack(List<VillageTroops> attackingTroops, List<VillageTroops> defendingTroops, Village defendingVillage) {
        Map<String, Integer> attackingAccumulatedDamage = new HashMap<>();
        Map<String, Integer> defendingAccumulatedDamage = new HashMap<>();
        System.out.println("Starting basic attack...");
        while (!isBattleOver(attackingTroops, defendingTroops)) {
            simulateBasicRound(attackingTroops, defendingTroops, attackingAccumulatedDamage, defendingAccumulatedDamage);
        }
        System.out.println("Resolving combat...");
        resolveCombat(attackingTroops, defendingTroops, defendingVillage);
    }
    public void simulateBasicRound(List<VillageTroops> attackingTroops, List<VillageTroops> defendingTroops,
                                    Map<String, Integer> attackingAccumulatedDamage, Map<String, Integer> defendingAccumulatedDamage) {
        // This order is a short version of what an "advanced" attack should do in the future
        TroopType.TroopCategory[] attackOrder = {
                TroopType.TroopCategory.SIEGE,
                TroopType.TroopCategory.ARCHER,
                TroopType.TroopCategory.CAVALRY,
                TroopType.TroopCategory.FOOT
        };

        // Loop through each category and apply damage
        for (TroopType.TroopCategory category : attackOrder) {
            int attackingDamage = calculateTotalDamage(filterTroopsByCategory(attackingTroops, category));
            int defendingDamage = calculateTotalDamage(filterTroopsByCategory(defendingTroops, category));

            applyBasicDamage(defendingTroops, attackingDamage, defendingAccumulatedDamage);
            applyBasicDamage(attackingTroops, defendingDamage, attackingAccumulatedDamage);
        }
    }

    public void applyBasicDamage(List<VillageTroops> troops, int totalDamage, Map<String, Integer> accumulatedDamage) {
        System.out.println("Total damage to be applied: " + totalDamage);
        for (VillageTroops troop : troops) {
            if (troop.getQuantity() <= 0) {
                continue;
            }

            String troopType = troop.getTroopType().toString();
            int previousDamage = accumulatedDamage.getOrDefault(troopType, 0);
            int newTotalDamage = previousDamage + totalDamage;

            System.out.println("Initial troop quantity: " + troop.getQuantity());

            int troopHealth = troop.getTroopType().getHealth();
            int troopsKilled = newTotalDamage / troopHealth;

            // Calculate surviving troops
            int survivingTroops = Math.max(0, troop.getQuantity() - troopsKilled);

            // Calculate remaining damage and update the map
            int remainingDamage = newTotalDamage % troopHealth;
            accumulatedDamage.put(troopType, remainingDamage);

            // Log and update
            System.out.println("Troops killed: " + troopsKilled);
            System.out.println("Surviving troops: " + survivingTroops);
            troop.setQuantity(survivingTroops);

            // Update totalDamage for the next troop type in the list
            totalDamage = remainingDamage;
        }
    }

    public int calculateTotalDamage(List<VillageTroops> troops) {
        int totalDamage = 0;
        for (VillageTroops troop : troops) {
            totalDamage += troop.getTroopType().getAttack() * troop.getQuantity();
        }
        return totalDamage;
    }

    public List<VillageTroops> filterTroopsByCategory(List<VillageTroops> troops, TroopType.TroopCategory category) {
        return troops.stream()
                .filter(t -> t.getTroopType().getCategory() == category)
                .collect(Collectors.toList());
    }



    public boolean isBattleOver(List<VillageTroops> attackingTroops, List<VillageTroops> defendingTroops) {
        boolean attackersDead = allTroopsDead(attackingTroops);
        boolean defendersDead = allTroopsDead(defendingTroops);
        System.out.println("Attackers all dead? " + attackersDead);
        System.out.println("Defenders all dead? " + defendersDead);
        return attackersDead || defendersDead;
    }

    public boolean allTroopsDead(List<VillageTroops> troops) {
        return troops.stream().allMatch(t -> t.getQuantity() <= 0);
    }


    public void resolveCombat(List<VillageTroops> attackingTroops, List<VillageTroops> defendingTroops, Village defendingVillage) {
        if (didAttackersWin(attackingTroops, defendingTroops)) {
            int totalCarryCapacity = calculateTotalCarryCapacity(attackingTroops);
            System.out.println("Total Carrying Capacity: " + totalCarryCapacity);  // Debugging line


            // Fetch available resources using the new method
            Map<TypeOfResource, Double> availableResources = resourceService.getAvailableResources(defendingVillage.getId());

            // Create a map to store the resources to be deducted
            Map<TypeOfResource, Double> resourcesToDeduct = new EnumMap<>(TypeOfResource.class);
            for (TypeOfResource resourceType : availableResources.keySet()) {
                double availableAmount = availableResources.get(resourceType);
                System.out.println("Resource Type: " + resourceType + ", Available Amount: " + availableAmount);  // Debugging line
                double amountToDeduct = Math.min(availableAmount, totalCarryCapacity / 4.0);
                resourcesToDeduct.put(resourceType, amountToDeduct);
            }

            System.out.println("Available Resources: " + availableResources);  // Debugging line

            // Deduct the resources from the defending village
            resourceService.deductResources(defendingVillage.getId(), resourcesToDeduct);
        } else {
            System.out.println("Defenders won.");
            // Defenders win: handle consequences here
        }
    }


    public int calculateTotalCarryCapacity(List<VillageTroops> attackingTroops) {
        return attackingTroops.stream().mapToInt(t -> t.getTroopType().getCarryCapacity() * t.getQuantity()).sum();
    }

    public boolean didAttackersWin(List<VillageTroops> attackingTroops, List<VillageTroops> defendingTroops) {
        return allTroopsDead(defendingTroops);
    }


}
