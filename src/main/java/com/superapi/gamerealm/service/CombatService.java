package com.superapi.gamerealm.service;


import com.superapi.gamerealm.model.Village;
import com.superapi.gamerealm.model.resources.Resources;
import com.superapi.gamerealm.model.troop.TroopType;
import com.superapi.gamerealm.model.troop.VillageTroops;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;


@Service
public class CombatService {


    public CombatService() {

    }

    public void advancedAttack(List<VillageTroops> attackingTroops, List<VillageTroops> defendingTroops, Village defendingVillage) {
        // uh oh
        int wallLevel = defendingVillage.getBuildings().get(0).getLevel();
        //this again
        Resources defendingResources = defendingVillage.getResources().get(0);
        boolean battleOver = false;

        while (!battleOver) {
            simulateRound(attackingTroops, defendingTroops, wallLevel);
            battleOver = isBattleOver(attackingTroops, defendingTroops);
        }

        if (didAttackersWin(attackingTroops, defendingTroops)) {
            handleAdvancedResourceSpoils(defendingResources, attackingTroops);
        } else {
            // Additional logic for defenders winning could go here
        }
    }
    // Simulate one round of combat
    private void simulateRound(List<VillageTroops> attackingTroops, List<VillageTroops> defendingTroops, int wallLevel) {
        Random random = new Random();

        for (VillageTroops attacker : attackingTroops) {
            TroopType attackerType = attacker.getTroopType();

            for (VillageTroops defender : defendingTroops) {
                TroopType defenderType = defender.getTroopType();
                int defenderArmor = defenderType.getArmor() * defender.getQuantity() + wallLevel * 10;

                int damageToDefender = attackerType.getAttack() * attacker.getQuantity() - defenderArmor;

                if (attackerType.getCategory() == TroopType.TroopCategory.ARCHER && defenderType.getCategory() == TroopType.TroopCategory.FOOT) {
                    damageToDefender *= 1.2;
                } else if (attackerType.getCategory() == TroopType.TroopCategory.ARCHER && defenderType.getCategory() == TroopType.TroopCategory.CAVALRY) {
                    damageToDefender *= 0.8;
                }

                int randomInt = random.nextInt(100);
                if (randomInt < 5) {
                    damageToDefender = 0;  // Missed
                } else if (randomInt >= 95) {
                    damageToDefender *= 2;  // Critical hit
                }

                int remainingHealth = defender.getQuantity() * defenderType.getHealth() - damageToDefender;
                defender.setQuantity(Math.max(0, remainingHealth / defenderType.getHealth()));
            }
        }
    }

    private boolean isBattleOver(List<VillageTroops> attackingTroops, List<VillageTroops> defendingTroops) {
        return attackingTroops.stream().allMatch(t -> t.getQuantity() <= 0) || defendingTroops.stream().allMatch(t -> t.getQuantity() <= 0);
    }

    private boolean didAttackersWin(List<VillageTroops> attackingTroops, List<VillageTroops> defendingTroops) {
        return defendingTroops.stream().allMatch(t -> t.getQuantity() <= 0);
    }

    private void handleAdvancedResourceSpoils(Resources defendingResources, List<VillageTroops> survivingAttackers) {
        double plunderRate = 0.1 * survivingAttackers.stream().mapToInt(VillageTroops::getQuantity).sum();

        defendingResources.setWood((double) (defendingResources.getWood() * (1 - plunderRate)));
        defendingResources.setWheat((double) (defendingResources.getWheat() * (1 - plunderRate)));
        defendingResources.setStone((double) (defendingResources.getStone() * (1 - plunderRate)));
        defendingResources.setGold((double) (defendingResources.getGold() * (1 - plunderRate)));
    }


}



