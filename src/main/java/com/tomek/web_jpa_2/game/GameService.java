package com.tomek.web_jpa_2.game;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class GameService {

	private GameRepository gameRepository;

	public GameService(GameRepository gameRepository) {
		this.gameRepository = gameRepository;
	}

	public List<Game> findGames() {
		return gameRepository.findAll();
	}

	public Game findById(long id) {
		Optional<Game> optGame = gameRepository.findById(id);

		return optGame.orElseThrow(() -> new GameNotFoundException());
	}

	public Game saveGame(Game game) {
		return gameRepository.save(game);
	}

	public Game updateGame(Game game) {
		return gameRepository.save(game);
	}

	public void deleteById(Long id) {
		// if bad id: org.springframework.dao.EmptyResultDataAccessException:
		gameRepository.deleteById(id);
	}

}
