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