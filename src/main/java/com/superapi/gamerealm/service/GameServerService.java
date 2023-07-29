package com.superapi.gamerealm.service;

import com.superapi.gamerealm.model.GameServer;

public interface GameServerService {

    GameServer createGameServerIfNotExists();

    boolean isGameServerInitialized();
}




