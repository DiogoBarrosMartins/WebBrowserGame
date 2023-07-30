package com.superapi.gamerealm.service;

import com.superapi.gamerealm.model.GameServer;

public interface GameServerService {

    void serve();

    GameServer startGameIfNoGameExists();
    boolean isGame();
}




