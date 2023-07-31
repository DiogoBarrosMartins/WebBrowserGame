package com.superapi.gamerealm.repository;

import com.superapi.gamerealm.model.Village;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VillageRepository extends JpaRepository<Village, Long> {
    @Query("SELECT v FROM Village v WHERE v.account.id = :accountId")
    List<Village> findAllByAccountId(Long accountId);

    Village findByCoordinatesXAndCoordinatesY(int x, int y);
    @Query("SELECT v FROM Village v WHERE v.account.username = :username")
    Optional<Village> findByAccountUsername(String username);
}