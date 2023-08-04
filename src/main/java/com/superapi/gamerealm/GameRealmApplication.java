package com.superapi.gamerealm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("com.superapi.gamerealm.model")
@EnableJpaRepositories("com.superapi.gamerealm.repository")
public class GameRealmApplication {
	public static void main(String[] args) {
		SpringApplication.run(GameRealmApplication.class, args);
	}
}
