package com.superapi.gamerealm.service;

import com.superapi.gamerealm.model.Village;
import com.superapi.gamerealm.model.troop.TroopType;
import com.superapi.gamerealm.model.troop.VillageTroops;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;




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
     */
    /**
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
     */



    private void simulateRound(List<VillageTroops> attackingTroops, List<VillageTroops> defendingTroops, int wallLevel) {
        // Separate the troops into categories for the attack order
        Map<TroopType.TroopCategory, List<VillageTroops>> attackingByCategory = filterTroopsByCategory(attackingTroops);
        Map<TroopType.TroopCategory, List<VillageTroops>> defendingByCategory = filterTroopsByCategory(defendingTroops);

        // Define attack order based on comments
        TroopType.TroopCategory[] attackOrder = {
                TroopType.TroopCategory.SIEGE, TroopType.TroopCategory.SIEGE,
                TroopType.TroopCategory.ARCHER, TroopType.TroopCategory.CAVALRY,
                TroopType.TroopCategory.CAVALRY, TroopType.TroopCategory.ARCHER,
                TroopType.TroopCategory.FOOT, TroopType.TroopCategory.FOOT
        };

        // Iterate over each round, alternating between attacker and defender
        for (int i = 0; i < attackOrder.length; i++) {
            List<VillageTroops> currentAttackers = attackingByCategory.getOrDefault(attackOrder[i], new ArrayList<>());
            List<VillageTroops> currentDefenders = defendingByCategory.getOrDefault(attackOrder[i], new ArrayList<>());

            if (i % 2 == 0) {  // Attacking turn
                int totalDamage = calculateTotalDamage(currentAttackers);
                distributeDamage(totalDamage, currentDefenders);
            } else {  // Defending turn
                int totalDamage = calculateTotalDamage(currentDefenders);
                distributeDamage(totalDamage, currentAttackers);
            }
        }
    }
    public void advancedAttack(List<VillageTroops> attackingTroops, List<VillageTroops> defendingTroops, Village defendingVillage) {
        boolean battleOver = false;

        while (!allTroopsDead(attackingTroops) && !allTroopsDead(defendingTroops)) {
            simulateRound(attackingTroops, defendingTroops);
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
            int damageToDeal = Math.min(totalDamage, randomTroop.getTroopType().getHealth() - randomTroop.getCurrentHealth());
            randomTroop.setHealth(randomTroop.getCurrentHealth() - damageToDeal);
            if (randomTroop.getCurrentHealth() <= 0) {
                randomTroop.setQuantity(randomTroop.getQuantity() - 1);
                randomTroop.setCurrentHealth(randomTroop.getTroopType().getHealth());
            }
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

    // Calculate the total damage done by a list of troops
    private int calculateTotalDamage(List<VillageTroops> troops) {
        int totalDamage = 0;
        for (VillageTroops troop : troops) {
            totalDamage += troop.getTroopType().getAttack() * troop.getQuantity();
        }
        return totalDamage;
    }


    // Checks if the battle is over
    private boolean isBattleOver(List<VillageTroops> attackingTroops, List<VillageTroops> defendingTroops) {
        return attackingTroops.stream().allMatch(t -> t.getQuantity() <= 0) || defendingTroops.stream().allMatch(t -> t.getQuantity() <= 0);
    }

    // Resolve the combat and apply the results
    private void resolveCombat(List<VillageTroops> attackingTroops, List<VillageTroops> defendingTroops, Village defendingVillage) {
        if (didAttackersWin(attackingTroops, defendingTroops)) {
            // Attackers win: handle resource spoils and other consequences here
        } else {
            // Defenders win: handle consequences here
        }
    }
    private boolean allTroopsDead(List<VillageTroops> troops) {
        return troops.stream().allMatch(t -> t.getQuantity() <= 0);
    }

    // Determine if the attackers won
    private boolean didAttackersWin(List<VillageTroops> attackingTroops, List<VillageTroops> defendingTroops) {
        return defendingTroops.stream().allMatch(t -> t.getQuantity() <= 0);
    }
}