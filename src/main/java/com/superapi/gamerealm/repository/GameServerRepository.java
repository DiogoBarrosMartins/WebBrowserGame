package com.superapi.gamerealm.repository;


import com.superapi.gamerealm.model.GameServer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameServerRepository extends JpaRepository<GameServer, Long> {
    GameServer findFirstByOrderByIdAsc();
}
