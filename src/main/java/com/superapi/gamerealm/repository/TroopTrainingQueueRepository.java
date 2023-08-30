package com.superapi.gamerealm.repository;

import com.superapi.gamerealm.model.troop.TroopTrainingQueue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TroopTrainingQueueRepository extends JpaRepository<TroopTrainingQueue, Long> {


    List<TroopTrainingQueue> findByVillageId(Long villageId);
    List<TroopTrainingQueue> findByVillageIdOrderByTrainingEndTimeAsc(Long villageId);

}
