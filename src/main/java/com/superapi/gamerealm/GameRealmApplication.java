package com.superapi.gamerealm;

import com.superapi.gamerealm.component.GameServerInitializer;
import com.superapi.gamerealm.service.GameServerServiceImpl;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GameRealmApplication {
	private final GameServerInitializer gameServerInitializer;

	@Autowired
	public GameRealmApplication(GameServerInitializer gameServerInitializer) {
		this.gameServerInitializer = gameServerInitializer;
	}
	public static void main(String[] args) {
		SpringApplication.run(GameRealmApplication.class, args);
	}
	@PostConstruct
	public void init() {
		// Call the initializeGameServer() method when the application starts
		gameServerInitializer.initializeGameServer();
	}
}
