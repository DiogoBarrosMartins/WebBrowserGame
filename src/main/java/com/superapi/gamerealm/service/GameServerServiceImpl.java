package com.superapi.gamerealm.service;

import com.superapi.gamerealm.model.GameServer;
import com.superapi.gamerealm.model.Grid;
import com.superapi.gamerealm.repository.GameServerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameServerServiceImpl implements GameServerService {

    private final GameServerRepository gameServerRepository;
    private final GridService gridService;
    private final AccountService accountService;

    @Autowired
    public GameServerServiceImpl(
            GameServerRepository gameServerRepository,
            GridService gridService,
            AccountService accountService) {
        this.gameServerRepository = gameServerRepository;
        this.gridService = gridService;
        this.accountService = accountService;
    }

    @Override
    public void serve() {
       if(isGame()){
       }
    }

    @Override
    public GameServer startGameIfNoGameExists() {
    return null;
    }

    @Override
    public boolean isGame() {
    return true;
    }
}
