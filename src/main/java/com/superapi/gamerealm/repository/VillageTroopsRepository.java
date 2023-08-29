package com.superapi.gamerealm.repository;

import com.superapi.gamerealm.model.troop.VillageTroops;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VillageTroopsRepository extends JpaRepository<VillageTroops, Long> {
}