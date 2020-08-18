package com.tomek.web_jpa_2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.tomek.web_jpa_2.game.Game;
import com.tomek.web_jpa_2.game.GameRepository;


@Component
public class GameDataLoader implements CommandLineRunner {
	private static Logger logger = LoggerFactory.getLogger(Application.class);
	@Autowired
	private GameRepository gameRepository;

	@Override
	public void run(String... args) throws Exception {	
		logger.info("----------------------> Saving darek user"); 
		Game game = new Game(1L,"Leauge of Legends","MOBA");
		gameRepository.save(game);
	}

}
