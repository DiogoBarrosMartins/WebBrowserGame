package com.superapi.gamerealm.repository;

import com.superapi.gamerealm.model.buildings.Construction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConstructionRepository extends JpaRepository<Construction, Integer> {


   Construction findByBuildingId(long buildingId);

   List<Construction> findByVillageId(Long villageId);
}
