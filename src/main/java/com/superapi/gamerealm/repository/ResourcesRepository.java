package com.superapi.gamerealm.repository;

import com.superapi.gamerealm.model.resources.Resources;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Optional;

public interface ResourcesRepository extends JpaRepository<Resources, Long> {

    @Override
    Optional<Resources> findById(Long villageId);

    @Modifying
    @Query("UPDATE Resources r SET r.wheat = :wheat WHERE r.id = :id")
    void updateWheat(@Param("id") Long id, @Param("wheat") BigDecimal wheat);

    @Modifying
    @Query("UPDATE Resources r SET r.wood = :wood WHERE r.id = :id")
    void updateWood(@Param("id") Long id, @Param("wood") BigDecimal wood);

    @Modifying
    @Query("UPDATE Resources r SET r.stone = :stone WHERE r.id = :id")
    void updateStone(@Param("id") Long id, @Param("stone") BigDecimal stone);

    @Modifying
    @Query("UPDATE Resources r SET r.gold = :gold WHERE r.id = :id")
    void updateGold(@Param("id") Long id, @Param("gold") BigDecimal gold);
}

