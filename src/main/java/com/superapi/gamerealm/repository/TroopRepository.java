package com.superapi.gamerealm.repository;

import com.superapi.gamerealm.model.troop.Troop;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TroopRepository extends JpaRepository<Troop, Long> {
}
