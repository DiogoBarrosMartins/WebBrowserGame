package com.superapi;

import com.superapi.gamerealm.model.Village;
import com.superapi.gamerealm.model.resources.Resources;
import com.superapi.gamerealm.model.troop.TroopType;
import com.superapi.gamerealm.model.troop.VillageTroops;
import com.superapi.gamerealm.service.CombatService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CombatServiceTest {

    private CombatService combatService;

    @BeforeEach
    public void setUp() {
        combatService = new CombatService();
    }

    @Test
    public void testAdvancedAttack() {
        Village defendingVillage = new Village(); // Initialize as needed
        Resources resources = new Resources();
        resources.setWood(100.0);
        resources.setStone(100.0);
        resources.setGold(100.0);
        resources.setWheat(100.0);
        defendingVillage.setResources(Arrays.asList(resources));

        List<VillageTroops> attackers = Arrays.asList(new VillageTroops(null, TroopType.HUMAN_FOOT_SOLDIER, 10));
        List<VillageTroops> defenders = Arrays.asList(new VillageTroops(null, TroopType.HUMAN_FOOT_SOLDIER, 5));

        combatService.advancedAttack(attackers, defenders, defendingVillage);

        // Assert that attackers have won (indirectly tests isBattleOver() and didAttackersWin())
        assertTrue(defenders.stream().allMatch(t -> t.getQuantity() <= 0));

        // Assert that resources have been plundered (indirectly tests handleAdvancedResourceSpoils())
        assertEquals(90.0, resources.getWood());  // Assuming 10% of resources are plundered
    }
    @Test
    public void testAdvancedAttack_DefendersWin() {
        Village defendingVillage = new Village(); // Initialize as needed
        Resources resources = new Resources();
        resources.setWood(100.0);
        resources.setStone(100.0);
        resources.setGold(100.0);
        resources.setWheat(100.0);
        defendingVillage.setResources(Arrays.asList(resources));

        List<VillageTroops> attackers = Arrays.asList(new VillageTroops(null, TroopType.HUMAN_FOOT_SOLDIER, 5));
        List<VillageTroops> defenders = Arrays.asList(new VillageTroops(null, TroopType.HUMAN_FOOT_SOLDIER, 10));

        combatService.advancedAttack(attackers, defenders, defendingVillage);

        // Indirectly test isBattleOver() and didAttackersWin() by asserting that defenders have won
        assertTrue(attackers.stream().allMatch(t -> t.getQuantity() <= 0));

        // Indirectly test handleAdvancedResourceSpoils() by asserting that resources have not been plundered
        assertEquals(100.0, resources.getWood());  // Assuming no resources are plundered if defenders win
    }

    @Test
    public void testSimulateRound() {
        // Setup
        Village defendingVillage = new Village();
        Resources resources = new Resources();
        resources.setWood(100.0);
        resources.setStone(100.0);
        resources.setGold(100.0);
        resources.setWheat(100.0);
        defendingVillage.setResources(Arrays.asList(resources));
        List<VillageTroops> attackers = Arrays.asList(new VillageTroops(null, TroopType.HUMAN_FOOT_SOLDIER, 10));
        List<VillageTroops> defenders = Arrays.asList(new VillageTroops(null, TroopType.HUMAN_FOOT_SOLDIER, 5));

        // Act
        combatService.advancedAttack(attackers, defenders, defendingVillage);

        // Assert: You can indirectly test that `simulateRound` is doing its job correctly by checking the state of troops after the attack.
        assertTrue(defenders.stream().allMatch(t -> t.getQuantity() <= 0));
    }


}