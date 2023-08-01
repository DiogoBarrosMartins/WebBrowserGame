package com.superapi.gamerealm.repository;

import com.superapi.gamerealm.model.resources.Resources;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface ResourcesRepository extends JpaRepository <Resources, Long> {

    @Modifying
    @Query("UPDATE Resources r SET r.amount = :wheat WHERE r.type = 'WHEAT'")
    void updateWheat(BigDecimal wheat);

    @Modifying
    @Query("UPDATE Resources r SET r.amount = :wood WHERE r.type = 'WOOD'")
    void updateWood(BigDecimal wood);

    @Modifying
    @Query("UPDATE Resources r SET r.amount = :stone WHERE r.type = 'STONE'")
    void updateStone(BigDecimal stone);

    @Modifying
    @Query("UPDATE Resources r SET r.amount = :gold WHERE r.type = 'GOLD'")
    void updateGold(BigDecimal gold);
}

