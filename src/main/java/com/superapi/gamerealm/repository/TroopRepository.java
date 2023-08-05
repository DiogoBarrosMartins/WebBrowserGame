package com.superapi.gamerealm.repository;

import com.superapi.gamerealm.model.troop.Troop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TroopRepository extends JpaRepository<Troop, Long> {
}
