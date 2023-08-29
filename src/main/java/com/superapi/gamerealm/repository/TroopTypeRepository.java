package com.superapi.gamerealm.repository;

import com.superapi.gamerealm.model.troop.TroopType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TroopTypeRepository extends JpaRepository<TroopType, Long> {
}
