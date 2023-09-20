package com.superapi;
import com.superapi.gamerealm.model.Village;
import com.superapi.gamerealm.model.resources.TypeOfResource;
import com.superapi.gamerealm.model.troop.TroopType;
import com.superapi.gamerealm.model.troop.VillageTroops;
import com.superapi.gamerealm.service.CombatService;
import com.superapi.gamerealm.service.ResourceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.*;

public class CombatServiceTest {

        @InjectMocks
    private CombatService combatService;

    @Mock
    private ResourceService resourceService;

    @Mock
    private Village defendingVillage;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }





    @Test
    public void testBasicAttack_AttackersWin0() {


        Village targetVillage = new Village();
        targetVillage.setId(1L);  // Setting ID to avoid NullPointerException


        Village targetVillage2 = new Village();
        targetVillage.setId(2L);  // Setting ID to avoid NullPointerException
        // Prepare test data
        List<VillageTroops> attackingTroops = new ArrayList<>();
        attackingTroops.add(new VillageTroops(targetVillage2,TroopType.HUMAN_FOOT_SOLDIER, 10));
        attackingTroops.add(new VillageTroops(targetVillage2,TroopType.HUMAN_ARCHER_CORPS, 5));

        List<VillageTroops> defendingTroops = new ArrayList<>();
        defendingTroops.add(new VillageTroops(targetVillage,TroopType.HUMAN_FOOT_SOLDIER, 10));
        defendingTroops.add(new VillageTroops(targetVillage,TroopType.HUMAN_ARCHER_CORPS, 5));

        // Perform basic attack
        combatService.basicAttack(attackingTroops, defendingTroops, targetVillage);


    }
        @Test
        public void testBasicAttack_DefendersWin0() {
            // Prepare test data
            Village targetVillage = new Village();
            targetVillage.setId(1L);

            Village targetVillage2 = new Village();
            targetVillage2.setId(2L);

            List<VillageTroops> attackingTroops = new ArrayList<>();
            attackingTroops.add(new VillageTroops(targetVillage2, TroopType.HUMAN_FOOT_SOLDIER, 5));
            attackingTroops.add(new VillageTroops(targetVillage2, TroopType.HUMAN_ARCHER_CORPS, 2));

            List<VillageTroops> defendingTroops = new ArrayList<>();
            defendingTroops.add(new VillageTroops(targetVillage, TroopType.HUMAN_FOOT_SOLDIER, 10));
            defendingTroops.add(new VillageTroops(targetVillage, TroopType.HUMAN_ARCHER_CORPS, 5));

            doNothing().when(resourceService).deductResources(anyLong(), anyMap());

            // Perform basic attack
            combatService.basicAttack(attackingTroops, defendingTroops, targetVillage);

            // Verify behavior
            verify(resourceService, never()).deductResources(anyLong(), anyMap());
        }
    @Test
    public void testSimulateBasicRound() {
        // Prepare test data
        List<VillageTroops> attackingTroops = new ArrayList<>();
        // (add troops to the list)
        List<VillageTroops> defendingTroops = new ArrayList<>();
        // (add troops to the list)
        Map<String, Integer> attackingAccumulatedDamage = new HashMap<>();
        Map<String, Integer> defendingAccumulatedDamage = new HashMap<>();

        // Perform the method call
        combatService.simulateBasicRound(attackingTroops, defendingTroops, attackingAccumulatedDamage, defendingAccumulatedDamage);

        // Verify behavior (insert your assertions here)
    }

    @Test
    public void testApplyBasicDamage() {
        // Prepare test data
        List<VillageTroops> troops = new ArrayList<>();
        // (add troops to the list)
        int totalDamage = 100;
        Map<String, Integer> accumulatedDamage = new HashMap<>();

        // Perform the method call
        combatService.applyBasicDamage(troops, totalDamage, accumulatedDamage);

        // Verify behavior (insert your assertions here)
    }

    @Test
    public void testIsBattleOver_AttackersDead() {
        // Prepare test data
        List<VillageTroops> attackingTroops = List.of(new VillageTroops(null, TroopType.HUMAN_FOOT_SOLDIER, 0));
        List<VillageTroops> defendingTroops = List.of(new VillageTroops(null, TroopType.HUMAN_FOOT_SOLDIER, 10));

        // Perform the method call & Verify behavior
        assertTrue(combatService.isBattleOver(attackingTroops, defendingTroops));
    }

    @Test
    public void testIsBattleOver_DefendersDead() {
        // Prepare test data
        List<VillageTroops> attackingTroops = List.of(new VillageTroops(null, TroopType.HUMAN_FOOT_SOLDIER, 10));
        List<VillageTroops> defendingTroops = List.of(new VillageTroops(null, TroopType.HUMAN_FOOT_SOLDIER, 0));

        // Perform the method call & Verify behavior
        assertTrue(combatService.isBattleOver(attackingTroops, defendingTroops));
    }

    @Test
    public void testAllTroopsDead() {
        // Prepare test data
        List<VillageTroops> troops = List.of(new VillageTroops(null, TroopType.HUMAN_FOOT_SOLDIER, 0));

        // Perform the method call & Verify behavior
        assertTrue(combatService.allTroopsDead(troops));
    }

    @Test
    public void testResolveCombat_AttackersWin() {
        // Prepare test data
        List<VillageTroops> attackingTroops = new ArrayList<>();
        // (add troops to the list)
        List<VillageTroops> defendingTroops = new ArrayList<>();
        // (add troops to the list)

        // Mock behavior
        when(defendingVillage.getId()).thenReturn(1L);

        // Perform the method call
        combatService.resolveCombat(attackingTroops, defendingTroops, defendingVillage);

        // Verify behavior (insert your assertions or Mockito's verify here)
    }

    @Test
    public void testResolveCombat_DefendersWin() {
        // Prepare test data
        List<VillageTroops> attackingTroops = new ArrayList<>();
        // (add troops to the list)
        List<VillageTroops> defendingTroops = new ArrayList<>();
        // (add troops to the list)

        // Mock behavior
        when(defendingVillage.getId()).thenReturn(1L);

        // Perform the method call
        combatService.resolveCombat(attackingTroops, defendingTroops, defendingVillage);

        // Verify behavior (insert your assertions or Mockito's verify here)
    }

    @Test
    public void testSimulateBasicRound2() {
        // Prepare test data
        Village defendingVillage = new Village();
        List<VillageTroops> attackingTroops = Arrays.asList(
                new VillageTroops(defendingVillage, TroopType.HUMAN_FOOT_SOLDIER, 5),
                new VillageTroops(defendingVillage, TroopType.HUMAN_ARCHER_CORPS, 5)
        );
        List<VillageTroops> defendingTroops = Arrays.asList(
                new VillageTroops(defendingVillage, TroopType.ORC_WARRIORS, 5),
                new VillageTroops(defendingVillage, TroopType.ORC_SHADOW_ARCHERS, 5)
        );
        Map<String, Integer> attackingAccumulatedDamage = new HashMap<>();
        Map<String, Integer> defendingAccumulatedDamage = new HashMap<>();

        // Call simulateBasicRound
        combatService.simulateBasicRound(attackingTroops, defendingTroops, attackingAccumulatedDamage, defendingAccumulatedDamage);

        // Assertions to verify behavior can be specific to your implementation
        // For example, verify that the troop count has decreased
    }

    @Test
    public void testIsBattleOver() {
        // Prepare test data
        Village defendingVillage = new Village();
        List<VillageTroops> attackingTroops = Arrays.asList(
                new VillageTroops(defendingVillage, TroopType.HUMAN_FOOT_SOLDIER, 5),
                new VillageTroops(defendingVillage, TroopType.HUMAN_ARCHER_CORPS, 5)
        );
        List<VillageTroops> defendingTroops = Arrays.asList(
                new VillageTroops(defendingVillage, TroopType.ORC_WARRIORS, 5),
                new VillageTroops(defendingVillage, TroopType.ORC_SHADOW_ARCHERS, 5)
        );

        // Call isBattleOver
        boolean result = combatService.isBattleOver(attackingTroops, defendingTroops);

        // Assertions
        assertFalse(result);
    }

    @Test
    public void testBasicAttack_AttackersWin() {

        // Arrange
        ResourceService mockResourceService = mock(ResourceService.class);
        CombatService combatService = new CombatService(mockResourceService);
        // Prepare test data
        Village attackingVillage = new Village();
        attackingVillage.setId(1L);

        Village defendingVillage = new Village();
        defendingVillage.setId(2L);

        List<VillageTroops> attackingTroops = new ArrayList<>();
        attackingTroops.add(new VillageTroops(attackingVillage, TroopType.HUMAN_FOOT_SOLDIER, 10));
        attackingTroops.add(new VillageTroops(attackingVillage, TroopType.HUMAN_ARCHER_CORPS, 5));

        List<VillageTroops> defendingTroops = new ArrayList<>();
        defendingTroops.add(new VillageTroops(defendingVillage, TroopType.HUMAN_FOOT_SOLDIER, 2));
        defendingTroops.add(new VillageTroops(defendingVillage, TroopType.HUMAN_ARCHER_CORPS, 1));

        // Perform basic attack
        combatService.basicAttack(attackingTroops, defendingTroops, defendingVillage);

        // Assertions
        // Assuming you have a way to get the result back or check the final state
        assertTrue(combatService.allTroopsDead(defendingTroops));  // Replace with the actual method to check if all troops are dead
        assertFalse(combatService.allTroopsDead(attackingTroops)); // Replace with the actual method to check if all troops are dead
        // Assume these are the available resources in the defending village
        Map<TypeOfResource, Double> availableResources = new HashMap<>();
        availableResources.put(TypeOfResource.WOOD, 500.0);
        availableResources.put(TypeOfResource.WHEAT, 500.0);
        availableResources.put(TypeOfResource.STONE, 500.0);
        availableResources.put(TypeOfResource.GOLD, 500.0);

        when(mockResourceService.getAvailableResources(anyLong())).thenReturn(availableResources);


    }

    @Test
    public void testBasicAttack_DefendersWin() {
        // Prepare test data similar to the previous test but make sure defenders win
        Village attackingVillage = new Village();
        attackingVillage.setId(1L);

        Village defendingVillage = new Village();
        defendingVillage.setId(2L);

        List<VillageTroops> attackingTroops = new ArrayList<>();
        attackingTroops.add(new VillageTroops(attackingVillage, TroopType.HUMAN_FOOT_SOLDIER, 2));
        attackingTroops.add(new VillageTroops(attackingVillage, TroopType.HUMAN_ARCHER_CORPS, 1));

        List<VillageTroops> defendingTroops = new ArrayList<>();
        defendingTroops.add(new VillageTroops(defendingVillage, TroopType.HUMAN_FOOT_SOLDIER, 10));
        defendingTroops.add(new VillageTroops(defendingVillage, TroopType.HUMAN_ARCHER_CORPS, 5));

        // Perform basic attack
        combatService.basicAttack(attackingTroops, defendingTroops, defendingVillage);

        // Assertions
        // Assuming you have a way to get the result back or check the final state
        assertTrue(combatService.allTroopsDead(attackingTroops));  // Replace with the actual method to check if all troops are dead
        assertFalse(combatService.allTroopsDead(defendingTroops)); // Replace with the actual method to check if all troops are dead

        // Make sure no resources are deducted from the defending village
        verify(resourceService, never()).deductResources(anyLong(), anyMap());
    }

    @Test
    public void testBasicAttack_MixedTroops_AttackersWin() {
        Village attackingVillage = new Village();
        attackingVillage.setId(1L);

        Village defendingVillage = new Village();
        defendingVillage.setId(2L);

        List<VillageTroops> attackingTroops = new ArrayList<>();
        attackingTroops.add(new VillageTroops(attackingVillage, TroopType.HUMAN_FOOT_SOLDIER, 10));
        attackingTroops.add(new VillageTroops(attackingVillage, TroopType.HUMAN_CAVALRY_KNIGHTS, 5));
        attackingTroops.add(new VillageTroops(attackingVillage, TroopType.HUMAN_SIEGE_ENGINEERS, 2));

        List<VillageTroops> defendingTroops = new ArrayList<>();
        defendingTroops.add(new VillageTroops(defendingVillage, TroopType.ORC_WARRIORS, 10));
        defendingTroops.add(new VillageTroops(defendingVillage, TroopType.ORC_SHADOW_ARCHERS, 5));
        System.out.println("Initial State:");
        System.out.println("Attacking Troops: " + attackingTroops);
        System.out.println("Defending Troops: " + defendingTroops);
        // Perform basic attack
        combatService.basicAttack(attackingTroops, defendingTroops, defendingVillage);
        System.out.println("Final State:");
        System.out.println("Attacking Troops: " + attackingTroops);
        System.out.println("Defending Troops: " + defendingTroops);
        // Assertions
        assertTrue(combatService.allTroopsDead(defendingTroops));
        assertFalse(combatService.allTroopsDead(attackingTroops));
    }

    @Test
    public void testBasicAttack_OnlyOneTypeOfTroops_DefendersWin() {
        Village attackingVillage = new Village();
        attackingVillage.setId(1L);

        Village defendingVillage = new Village();
        defendingVillage.setId(2L);

        List<VillageTroops> attackingTroops = new ArrayList<>();
        attackingTroops.add(new VillageTroops(attackingVillage, TroopType.HUMAN_FOOT_SOLDIER, 5));

        List<VillageTroops> defendingTroops = new ArrayList<>();
        defendingTroops.add(new VillageTroops(defendingVillage, TroopType.ORC_WARRIORS, 15));

        // Perform basic attack
        combatService.basicAttack(attackingTroops, defendingTroops, defendingVillage);

        // Assertions
        assertTrue(combatService.allTroopsDead(attackingTroops));
        assertFalse(combatService.allTroopsDead(defendingTroops));
    }

    @Test
    public void testBasicAttack_NoTroops() {
        Village attackingVillage = new Village();
        attackingVillage.setId(1L);

        Village defendingVillage = new Village();
        defendingVillage.setId(2L);

        List<VillageTroops> attackingTroops = new ArrayList<>();

        List<VillageTroops> defendingTroops = new ArrayList<>();

        // Perform basic attack
        combatService.basicAttack(attackingTroops, defendingTroops, defendingVillage);

        // Assertions
        assertTrue(combatService.allTroopsDead(attackingTroops));
        assertTrue(combatService.allTroopsDead(defendingTroops));
    }

    @Test
    public void testBasicAttack_MassiveTroops_AttackersWin() {
        // Prepare test data
        Village attackingVillage = new Village();
        attackingVillage.setId(1L);

        Village defendingVillage = new Village();
        defendingVillage.setId(2L);

        List<VillageTroops> attackingTroops = new ArrayList<>();
        attackingTroops.add(new VillageTroops(attackingVillage, TroopType.HUMAN_FOOT_SOLDIER, 1000000));

        List<VillageTroops> defendingTroops = new ArrayList<>();
        defendingTroops.add(new VillageTroops(defendingVillage, TroopType.HUMAN_FOOT_SOLDIER, 10));

        // Perform basic attack
        combatService.basicAttack(attackingTroops, defendingTroops, defendingVillage);

        // Assertions
        assertTrue(combatService.allTroopsDead(defendingTroops));
        assertFalse(combatService.allTroopsDead(attackingTroops));
    }

    @Test
    public void testBasicAttack_MassiveTroops_DefendersWin() {
        // Prepare test data
        Village attackingVillage = new Village();
        attackingVillage.setId(1L);

        Village defendingVillage = new Village();
        defendingVillage.setId(2L);

        List<VillageTroops> attackingTroops = new ArrayList<>();
        attackingTroops.add(new VillageTroops(attackingVillage, TroopType.HUMAN_FOOT_SOLDIER, 10));

        List<VillageTroops> defendingTroops = new ArrayList<>();
        defendingTroops.add(new VillageTroops(defendingVillage, TroopType.HUMAN_FOOT_SOLDIER, 1000000));

        // Perform basic attack
        combatService.basicAttack(attackingTroops, defendingTroops, defendingVillage);

        // Assertions
        assertTrue(combatService.allTroopsDead(attackingTroops));
        assertFalse(combatService.allTroopsDead(defendingTroops));
    }
    @Test
    public void testBasicAttack_MixedTroopTypes_AttackersWin() {
        // Prepare test data
        Village attackingVillage = new Village();
        attackingVillage.setId(1L);

        Village defendingVillage = new Village();
        defendingVillage.setId(2L);

        List<VillageTroops> attackingTroops = new ArrayList<>();
        attackingTroops.add(new VillageTroops(attackingVillage, TroopType.HUMAN_FOOT_SOLDIER, 100));
        attackingTroops.add(new VillageTroops(attackingVillage, TroopType.HUMAN_ARCHER_CORPS, 100));
        attackingTroops.add(new VillageTroops(attackingVillage, TroopType.HUMAN_CAVALRY_KNIGHTS, 100));

        List<VillageTroops> defendingTroops = new ArrayList<>();
        defendingTroops.add(new VillageTroops(defendingVillage, TroopType.ORC_WARRIORS, 10));

        combatService.basicAttack(attackingTroops, defendingTroops, defendingVillage);
        assertTrue(combatService.allTroopsDead(defendingTroops));
        assertFalse(combatService.allTroopsDead(attackingTroops));

    }
    @Test
    public void testBasicAttack_AllHumanVsAllOrc_LargeScaleBattle() {
        Village attackingVillage = new Village();
        attackingVillage.setId(1L);

        Village defendingVillage = new Village();
        defendingVillage.setId(2L);

        // All types of Human troops
        List<VillageTroops> attackingTroops = new ArrayList<>();
        attackingTroops.add(new VillageTroops(attackingVillage, TroopType.HUMAN_FOOT_SOLDIER, 100));
        attackingTroops.add(new VillageTroops(attackingVillage, TroopType.HUMAN_IMPERIAL_GUARD, 100));
        attackingTroops.add(new VillageTroops(attackingVillage, TroopType.HUMAN_FOOT_KNIGHT, 100));
        attackingTroops.add(new VillageTroops(attackingVillage, TroopType.HUMAN_FOOT_MAGES, 50));
        attackingTroops.add(new VillageTroops(attackingVillage, TroopType.HUMAN_ARCHER_CORPS, 200));
        attackingTroops.add(new VillageTroops(attackingVillage, TroopType.HUMAN_LONG_BOWMEN, 200));
        attackingTroops.add(new VillageTroops(attackingVillage, TroopType.HUMAN_CROSSBOWMEN, 100));
        attackingTroops.add(new VillageTroops(attackingVillage, TroopType.HUMAN_CAVALRY_KNIGHTS, 50));
        attackingTroops.add(new VillageTroops(attackingVillage, TroopType.HUMAN_LIGHT_CAVALRY, 50));
        attackingTroops.add(new VillageTroops(attackingVillage, TroopType.HUMAN_ROYAL_LANCERS, 20));
        attackingTroops.add(new VillageTroops(attackingVillage, TroopType.HUMAN_SIEGE_ENGINEERS, 10));
        attackingTroops.add(new VillageTroops(attackingVillage, TroopType.HUMAN_CATAPULTS, 5));
        attackingTroops.add(new VillageTroops(attackingVillage, TroopType.HUMAN_TREBUCHETS, 3));
        // All types of Orc troops, including the missing ones
        List<VillageTroops> defendingTroops = new ArrayList<>();
        defendingTroops.add(new VillageTroops(defendingVillage, TroopType.ORC_WARRIORS, 100));
        defendingTroops.add(new VillageTroops(defendingVillage, TroopType.ORC_BRUTE_WARRIORS, 100));
        defendingTroops.add(new VillageTroops(defendingVillage, TroopType.ORC_BLOODRAGE_BERSERKERS, 100));
        defendingTroops.add(new VillageTroops(defendingVillage, TroopType.ORC_SHAMAN_WARRIORS, 50));
        defendingTroops.add(new VillageTroops(defendingVillage, TroopType.ORC_SHADOW_ARCHERS, 200));
        defendingTroops.add(new VillageTroops(defendingVillage, TroopType.ORC_POISON_BOWMEN, 200));
        defendingTroops.add(new VillageTroops(defendingVillage, TroopType.ORC_EXPLOSIVE_ARCHERS, 100));
        defendingTroops.add(new VillageTroops(defendingVillage, TroopType.ORC_BLOODRIDERS, 50));
        defendingTroops.add(new VillageTroops(defendingVillage, TroopType.ORC_WAR_BOARS, 50));
        defendingTroops.add(new VillageTroops(defendingVillage, TroopType.ORC_WOLF_RAIDERS, 20));
        defendingTroops.add(new VillageTroops(defendingVillage, TroopType.ORC_SIEGE_GOLEMS, 10));
        defendingTroops.add(new VillageTroops(defendingVillage, TroopType.ORC_DEMOLISHERS, 5));
        defendingTroops.add(new VillageTroops(defendingVillage, TroopType.ORC_LAVA_RAMMERS, 3));

        // Perform basic attack

        combatService.basicAttack(attackingTroops, defendingTroops, defendingVillage);
        assertFalse(combatService.allTroopsDead(defendingTroops));
        assertTrue(combatService.allTroopsDead(attackingTroops));
    }
    @Test
    public void testBasicAttack_AllHumanVsAllElf_LargeScaleBattle() {
        Village attackingVillage = new Village();
        attackingVillage.setId(1L);

        Village defendingVillage = new Village();
        defendingVillage.setId(2L);


        // All types of Human troops
        List<VillageTroops> attackingTroops = new ArrayList<>();
        attackingTroops.add(new VillageTroops(attackingVillage, TroopType.HUMAN_FOOT_SOLDIER, 100));
        attackingTroops.add(new VillageTroops(attackingVillage, TroopType.HUMAN_IMPERIAL_GUARD, 100));
        attackingTroops.add(new VillageTroops(attackingVillage, TroopType.HUMAN_FOOT_KNIGHT, 100));
        attackingTroops.add(new VillageTroops(attackingVillage, TroopType.HUMAN_FOOT_MAGES, 50));
        attackingTroops.add(new VillageTroops(attackingVillage, TroopType.HUMAN_ARCHER_CORPS, 200));
        attackingTroops.add(new VillageTroops(attackingVillage, TroopType.HUMAN_LONG_BOWMEN, 200));
        attackingTroops.add(new VillageTroops(attackingVillage, TroopType.HUMAN_CROSSBOWMEN, 100));
        attackingTroops.add(new VillageTroops(attackingVillage, TroopType.HUMAN_CAVALRY_KNIGHTS, 50));
        attackingTroops.add(new VillageTroops(attackingVillage, TroopType.HUMAN_LIGHT_CAVALRY, 50));
        attackingTroops.add(new VillageTroops(attackingVillage, TroopType.HUMAN_ROYAL_LANCERS, 20));
        attackingTroops.add(new VillageTroops(attackingVillage, TroopType.HUMAN_SIEGE_ENGINEERS, 10));
        attackingTroops.add(new VillageTroops(attackingVillage, TroopType.HUMAN_CATAPULTS, 5));
        attackingTroops.add(new VillageTroops(attackingVillage, TroopType.HUMAN_TREBUCHETS, 3));
        List<VillageTroops> defendingTroops = new ArrayList<>();
        defendingTroops.add(new VillageTroops(defendingVillage, TroopType.ELVISH_SCOUTS, 100));
        defendingTroops.add(new VillageTroops(defendingVillage, TroopType.ELVISH_FOREST_ARCHERS, 100));
        defendingTroops.add(new VillageTroops(defendingVillage, TroopType.ELVISH_ELITE_RANGERS, 50));
        defendingTroops.add(new VillageTroops(defendingVillage, TroopType.ELVISH_DRUID_WARRIORS, 50));
        defendingTroops.add(new VillageTroops(defendingVillage, TroopType.ELVISH_WINDRIDER_ARCHERS, 200));
        defendingTroops.add(new VillageTroops(defendingVillage, TroopType.ELVISH_SILVERLEAF_BOWMEN, 200));
        defendingTroops.add(new VillageTroops(defendingVillage, TroopType.ELVISH_STORMRIDER_SNIPERS, 100));
        defendingTroops.add(new VillageTroops(defendingVillage, TroopType.ELVISH_GRYPHON_KNIGHTS, 50));
        defendingTroops.add(new VillageTroops(defendingVillage, TroopType.ELVISH_FOREST_RIDERS, 50));
        defendingTroops.add(new VillageTroops(defendingVillage, TroopType.ELVISH_MOONSHADOW_DRAGOONS, 20));
        defendingTroops.add(new VillageTroops(defendingVillage, TroopType.ELVISH_TREANT_SIEGE, 10));
        defendingTroops.add(new VillageTroops(defendingVillage, TroopType.ELVISH_EARTHEN_CATAPULTS, 5));
        defendingTroops.add(new VillageTroops(defendingVillage, TroopType.ELVISH_STARBREAKER_BALLISTAE, 3));

        // Perform basic attack
        combatService.basicAttack(attackingTroops, defendingTroops, defendingVillage);
        assertFalse(combatService.allTroopsDead(defendingTroops));
        assertTrue(combatService.allTroopsDead(attackingTroops));
    }
    @Test
    public void testBasicAttack_MixedTroopTypes_DefendersWin() {

        // Prepare test data
        Village attackingVillage = new Village();
        attackingVillage.setId(1L);

        Village defendingVillage = new Village();
        defendingVillage.setId(2L);
        List<VillageTroops> attackingTroops = new ArrayList<>();
        attackingTroops.add(new VillageTroops(attackingVillage, TroopType.HUMAN_FOOT_SOLDIER, 10));

        List<VillageTroops> defendingTroops = new ArrayList<>();
        defendingTroops.add(new VillageTroops(defendingVillage, TroopType.ORC_WARRIORS, 100));
        defendingTroops.add(new VillageTroops(defendingVillage, TroopType.ORC_SHADOW_ARCHERS, 100));
        defendingTroops.add(new VillageTroops(defendingVillage, TroopType.ORC_BLOODRIDERS, 100));

        // Perform basic attack
        combatService.basicAttack(attackingTroops, defendingTroops, defendingVillage);

        // Assertions
        assertTrue(combatService.allTroopsDead(attackingTroops));
        assertFalse(combatService.allTroopsDead(defendingTroops));
    }
}



