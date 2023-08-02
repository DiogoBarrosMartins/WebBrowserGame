package com.superapi.gamerealm.service;

import com.superapi.gamerealm.model.troop.Troop;

import java.util.List;

public interface CombatService {
    void attack(List<Troop> attackingTroops, List<Troop> defendingTroops);
}
