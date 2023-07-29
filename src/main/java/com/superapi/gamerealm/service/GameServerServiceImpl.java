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
    public GameServer createGameServerIfNotExists() {
        GameServer gameServer = gameServerRepository.findFirstByOrderByIdAsc();

        if (gameServer == null) {
            // If no game server exists, create a new one and initialize the grid
            gameServer = new GameServer();
            gameServer.setInitialized(false); // Set to true once the grid is initialized

            // Purge player accounts (Delete all player accounts)
            accountService.purgePlayerAccounts();

            // Initialize the grid with a 10x10 size and 5 barbarian villages
            Grid grid = gridService.createAndInitializeGrid(10, 10, 5);

            // Save the game server entity in the database
            gameServer = gameServerRepository.save(gameServer);
        }

        return gameServer;
    }

    @Override
    public boolean isGameServerInitialized() {
        GameServer gameServer = gameServerRepository.findFirstByOrderByIdAsc();
        return gameServer != null && gameServer.isInitialized();
    }
}
