package com.superapi.gamerealm.component;


import com.superapi.gamerealm.service.GameServerService;
import com.superapi.gamerealm.service.VillageService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class GameServerInitializer {

    private final GameServerService gameServerService;
    private final VillageService villageService;

    public GameServerInitializer(GameServerService gameServerService, VillageService villageService) {
        this.gameServerService = gameServerService;
        this.villageService = villageService;
    }

    @Transactional
    public void initializeGameServer() {
        gameServerService.createGameServerIfNotExists();

    }


}
