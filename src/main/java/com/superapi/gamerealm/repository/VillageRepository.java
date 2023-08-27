package com.superapi.gamerealm.repository;

import com.superapi.gamerealm.model.Village;
import com.superapi.gamerealm.model.buildings.Building;
import com.superapi.gamerealm.model.resources.Resources;
import com.superapi.gamerealm.model.troop.Troop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VillageRepository extends JpaRepository<Village, Long> {
    @Query("SELECT v FROM Village v WHERE v.account.id = :accountId")
    List<Village> findAllByAccountId(Long accountId);

    @Query("SELECT v FROM Village v WHERE v.account.username = ?1")
    Optional<Village> findVillageByUsername(String username);

    @Query("SELECT b FROM Building b WHERE b.village.account.username = ?1")
    List<Building> findBuildingsByVillageUsername(String username);
    @Query("SELECT r FROM Resources r WHERE r.village.account.username = ?1")
    List<Resources> findResourcesByVillageUsername(String username);

    @Query("SELECT t FROM Troop t WHERE t.village.account.username = ?1")
    List<Troop> findTroopsByVillageUsername(String username);
    @Query("SELECT v FROM Village v LEFT JOIN FETCH v.resources LEFT JOIN FETCH v.troops WHERE v.account.username = :username")
    Optional<Village> findVillageWithResourcesAndTroopsByUsername(@Param("username") String username);
    @Query("SELECT v FROM Village v WHERE v.x BETWEEN :minX AND :maxX AND v.y BETWEEN :minY AND :maxY")
    List<Village> findVillagesInArea(@Param("minX") int minX, @Param("maxX") int maxX, @Param("minY") int minY, @Param("maxY") int maxY);
    Village findByXAndY(int x, int y);
    @Query("SELECT v FROM Village v WHERE v.account.username = :username")
    List<Village> findByAccountUsername(String username);

}