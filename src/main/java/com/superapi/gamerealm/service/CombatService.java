package com.superapi.gamerealm.service;

import com.superapi.gamerealm.model.Village;
import com.superapi.gamerealm.model.resources.TypeOfResource;
import com.superapi.gamerealm.model.troop.TroopType;
import com.superapi.gamerealm.model.troop.VillageTroops;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**

@Service
public class CombatService {



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


    public void advancedAttack(List<VillageTroops> attackingTroops, List<VillageTroops> defendingTroops, Village defendingVillage) {
        boolean battleOver = false;
        while (!battleOver) {
            simulateRound(attackingTroops, defendingTroops);
            battleOver = isBattleOver(attackingTroops, defendingTroops);
        }
        resolveCombat(attackingTroops, defendingTroops, defendingVillage);
    }

    private void simulateRound(List<VillageTroops> attackingTroops, List<VillageTroops> defendingTroops) {
        distributeDamage(defendingTroops, calculateTotalDamage(filterTroopsByCategory(attackingTroops, TroopType.TroopCategory.SIEGE)));
        distributeDamage(attackingTroops, calculateTotalDamage(filterTroopsByCategory(defendingTroops, TroopType.TroopCategory.SIEGE)));
        distributeDamage(attackingTroops, calculateTotalDamage(filterTroopsByCategory(defendingTroops, TroopType.TroopCategory.ARCHER)));
        distributeDamage(defendingTroops, calculateTotalDamage(filterTroopsByCategory(attackingTroops, TroopType.TroopCategory.CAVALRY)));
        distributeDamage(attackingTroops, calculateTotalDamage(filterTroopsByCategory(defendingTroops, TroopType.TroopCategory.CAVALRY)));
        distributeDamage(defendingTroops, calculateTotalDamage(filterTroopsByCategory(attackingTroops, TroopType.TroopCategory.ARCHER)));
        distributeDamage(defendingTroops, calculateTotalDamage(filterTroopsByCategory(attackingTroops, TroopType.TroopCategory.FOOT)));
        distributeDamage(attackingTroops, calculateTotalDamage(filterTroopsByCategory(defendingTroops, TroopType.TroopCategory.FOOT)));
    }

    private void distributeDamage(List<VillageTroops> receivingTroops, int totalDamage) {
        Random rand = new Random();
        while (totalDamage > 0 && !allTroopsDead(receivingTroops)) {
            VillageTroops randomTroop = getRandomLivingTroop(receivingTroops, rand);
            int damageToDeal = Math.min(totalDamage, randomTroop.getTroopType().getHealth());
            int casualties = damageToDeal / randomTroop.getTroopType().getHealth();
            int remainingTroops = randomTroop.getQuantity() - casualties;
            randomTroop.setQuantity(Math.max(0, remainingTroops));
            totalDamage -= damageToDeal;
        }
    }

    private VillageTroops getRandomLivingTroop(List<VillageTroops> troops) {
        Random rand = new Random();
        List<VillageTroops> livingTroops = troops.stream().filter(t -> t.getQuantity() > 0).collect(Collectors.toList());
        if (livingTroops.isEmpty()) {
            return null;
        }
        return livingTroops.get(rand.nextInt(livingTroops.size()));
    }


    // Separates troops by category
    private List<VillageTroops> filterTroopsByCategory(List<VillageTroops> troops, TroopType.TroopCategory category) {
        return troops.stream()
                .filter(t -> t.getTroopType().getCategory() == category)
                .collect(Collectors.toList());
    }

    private int calculateTotalDamage(List<VillageTroops> troops) {
        int totalDamage = 0;
        for (VillageTroops troop : troops) {
            totalDamage += troop.getTroopType().getAttack() * troop.getQuantity();
        }
        return totalDamage;
    }

    private List<VillageTroops> filterTroopsByCategory(List<VillageTroops> troops, TroopType.TroopCategory category) {
        return troops.stream()
                .filter(troop -> troop.getTroopType().getCategory() == category)
                .collect(Collectors.toList());
    }

    private boolean isBattleOver(List<VillageTroops> attackingTroops, List<VillageTroops> defendingTroops) {
        return allTroopsDead(attackingTroops) || allTroopsDead(defendingTroops);
    }

    private boolean allTroopsDead(List<VillageTroops> troops) {
        return troops.stream().allMatch(t -> t.getQuantity() <= 0);
    }

    private VillageTroops getRandomLivingTroop(List<VillageTroops> troops, Random rand) {
        List<VillageTroops> livingTroops = troops.stream()
                .filter(t -> t.getQuantity() > 0)
                .collect(Collectors.toList());
        return livingTroops.get(rand.nextInt(livingTroops.size()));
    }
    // Determine if the attackers won
    private boolean didAttackersWin(List<VillageTroops> attackingTroops, List<VillageTroops> defendingTroops) {
        return defendingTroops.stream().allMatch(t -> t.getQuantity() <= 0);
    }
}

**/





@Service
public class CombatService {

    private final Map<String, Integer> accumulatedDamage = new HashMap<>();
    private final ResourceService resourceService;

    public CombatService(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    public void basicAttack(List<VillageTroops> attackingTroops, List<VillageTroops> defendingTroops, Village defendingVillage) {
        System.out.println("Starting basic attack...");
        boolean battleOver = false;
        while (!battleOver) {
            System.out.println("Simulating basic round...");

            // Skip round if total attacking or defending damage is zero
            int totalAttackingDamage = calculateTotalDamage(attackingTroops);
            int totalDefendingDamage = calculateTotalDamage(defendingTroops);
            if (totalAttackingDamage == 0 || totalDefendingDamage == 0) {
                System.out.println("Skipping zero damage round.");
                break;
            }

            simulateBasicRound(attackingTroops, defendingTroops);
            battleOver = isBattleOver(attackingTroops, defendingTroops);
            System.out.println("Battle over? " + battleOver);
        }
        System.out.println("Resolving combat...");
        resolveCombat(attackingTroops, defendingTroops, defendingVillage);
    }


    private void simulateBasicRound(List<VillageTroops> attackingTroops, List<VillageTroops> defendingTroops) {

        // Attack order simplified
        TroopType.TroopCategory[] attackOrder = {
                TroopType.TroopCategory.SIEGE,
                TroopType.TroopCategory.ARCHER,
                TroopType.TroopCategory.CAVALRY,
                TroopType.TroopCategory.FOOT
        };

        for (TroopType.TroopCategory category : attackOrder) {
            System.out.println("Category: " + category);

            int attackingDamage = calculateTotalDamage(filterTroopsByCategory(attackingTroops, category));
            int defendingDamage = calculateTotalDamage(filterTroopsByCategory(defendingTroops, category));

            System.out.println("Calculating attacking damage: " + attackingDamage);
            System.out.println("Calculating defending damage: " + defendingDamage);

            applyBasicDamage(defendingTroops, attackingDamage);
            applyBasicDamage(attackingTroops, defendingDamage);
        }
    }

    private void applyBasicDamage(List<VillageTroops> troops, int totalDamage) {
        System.out.println("Applying basic damage: " + totalDamage);
        for (VillageTroops troop : troops) {
            if (troop.getQuantity() == 0) {
                System.out.println("Skipping, troop quantity is zero.");
                continue;
            }

            String troopType = troop.getTroopType().toString();
            int previousDamage = accumulatedDamage.getOrDefault(troopType, 0);

            System.out.println("Troop Type: " + troopType + ", Quantity: " + troop.getQuantity());

            int newTotalDamage = previousDamage + totalDamage;
            int damagePerTroop = newTotalDamage / troop.getQuantity();

            System.out.println("Damage per troop: " + damagePerTroop);

            int troopsKilled = damagePerTroop / troop.getTroopType().getHealth();
            System.out.println("Troops killed: " + troopsKilled);

            int survivingTroops = troop.getQuantity() - troopsKilled;
            System.out.println("Surviving troops: " + survivingTroops);

            int remainingDamage = newTotalDamage - (troopsKilled * troop.getTroopType().getHealth());
            accumulatedDamage.put(troopType, remainingDamage);

            troop.setQuantity(Math.max(0, survivingTroops));
        }
    }


    private int calculateTotalDamage(List<VillageTroops> troops) {
        return troops.stream().mapToInt(t -> t.getTroopType().getAttack() * t.getQuantity()).sum();
    }

    private List<VillageTroops> filterTroopsByCategory(List<VillageTroops> troops, TroopType.TroopCategory category) {
        return troops.stream()
                .filter(t -> t.getTroopType().getCategory() == category)
                .collect(Collectors.toList());
    }

    private boolean isBattleOver(List<VillageTroops> attackingTroops, List<VillageTroops> defendingTroops) {
        return allTroopsDead(attackingTroops) || allTroopsDead(defendingTroops);
    }

    private boolean allTroopsDead(List<VillageTroops> troops) {
        return troops.stream().allMatch(t -> t.getQuantity() <= 0);
    }


    private void resolveCombat(List<VillageTroops> attackingTroops, List<VillageTroops> defendingTroops, Village defendingVillage) {
        if (didAttackersWin(attackingTroops, defendingTroops)) {
            int totalCarryCapacity = calculateTotalCarryCapacity(attackingTroops);
            System.out.println("Attackers won.");
            // Divide the total carrying capacity by 4 to distribute among the resources
            double perResourceCapacity = (double) totalCarryCapacity / 4;

            // Create a map to store the resources to be deducted
            Map<TypeOfResource, Double> resourcesToDeduct = new HashMap<>();
            resourcesToDeduct.put(TypeOfResource.WOOD, perResourceCapacity);
            resourcesToDeduct.put(TypeOfResource.WHEAT, perResourceCapacity);
            resourcesToDeduct.put(TypeOfResource.STONE, perResourceCapacity);
            resourcesToDeduct.put(TypeOfResource.GOLD, perResourceCapacity);

            // Deduct the resources from the defending village
            resourceService.deductResources(defendingVillage.getId(), resourcesToDeduct);
        } else {
            System.out.println("Defenders won.");
            // Defenders win: handle consequences here
        }
    }


    private int calculateTotalCarryCapacity(List<VillageTroops> attackingTroops) {
        int totalCarryCapacity = 0;
        for (VillageTroops troop : attackingTroops) {
            totalCarryCapacity += troop.getTroopType().getCarryCapacity() * troop.getQuantity();
        }
        return totalCarryCapacity;
    }


    public boolean didAttackersWin(List<VillageTroops> attackingTroops, List<VillageTroops> defendingTroops) {
        return allTroopsDead(defendingTroops);
    }


}
