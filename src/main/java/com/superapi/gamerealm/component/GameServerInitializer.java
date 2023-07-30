package com.superapi.gamerealm.component;


import com.superapi.gamerealm.service.GameServerService;
import com.superapi.gamerealm.service.VillageService;
import org.springframework.stereotype.Component;

@Component
public class GameServerInitializer {

    private final GameServerService gameServerService;

    public GameServerInitializer(GameServerService gameServerService, VillageService villageService) {
        this.gameServerService = gameServerService;
    }
    public void initializeGameServer() {
        gameServerService.serve();
    }

}
