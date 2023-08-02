package com.superapi.gamerealm.service;

import com.superapi.gamerealm.model.troop.Troop;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
    public class CombatServiceImpl implements CombatService{
        public void attack(List<Troop> attackingTroops, List<Troop> defendingTroops) {
            int attackingPower = calculatePower(attackingTroops);
            int defendingPower = calculatePower(defendingTroops);

            int attackingCasualties = calculateCasualties(attackingPower, defendingPower);
            int defendingCasualties = calculateCasualties(defendingPower, attackingPower);

            removeCasualties(attackingTroops, attackingCasualties);
            removeCasualties(defendingTroops, defendingCasualties);
        }

        private int calculatePower(List<Troop> troops) {
            return troops.stream()
                    .mapToInt(Troop::getAttack)
                    .sum();
        }

        private int calculateCasualties(int attackingPower, int defendingPower) {
            double ratio = (double) attackingPower / defendingPower;
            double casualties = Math.min(ratio, 1.0) * 0.5;
            return (int) Math.round(casualties * 100);
        }

        private void removeCasualties(List<Troop> troops, int casualties) {
            int totalHealth = troops.stream()
                    .mapToInt(Troop::getHealth)
                    .sum();

            int casualtiesLeft = casualties;

            for (Troop troop : troops) {
                int troopHealth = troop.getHealth();
                int troopCasualties = (int) Math.round((double) troopHealth / totalHealth * casualties);

                if (troopCasualties >= troopHealth) {
                    troops.remove(troop);
                } else {
                    troop.setHealth(troopHealth - troopCasualties);
                    casualtiesLeft -= troopCasualties;
                }

                if (casualtiesLeft <= 0) {
                    break;
                }
            }
        }
    }




