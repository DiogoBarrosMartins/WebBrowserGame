package com.superapi.gamerealm;

import com.superapi.gamerealm.component.GameServerInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class Main {
	private final GameServerInitializer gameServerInitializer;

	@Autowired
	public Main(GameServerInitializer gameServerInitializer) {
		this.gameServerInitializer = gameServerInitializer;
	}
	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}
}
