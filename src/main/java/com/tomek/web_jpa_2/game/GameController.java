package com.tomek.web_jpa_2.game;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("games")
public class GameController {
	private static Logger logger = LoggerFactory.getLogger(GameController.class);

	@Autowired
	private GameService gameService;
	
	@GetMapping("")
	public List<Game> showAllGames() {
		logger.info("----------------------> Entering showAllGames");
		return gameService.findGames();
	}

	@PostMapping("")
	public Game addGame(@RequestBody Game game) {
		logger.info("----------------------> Entering addGame");
		
		return gameService.saveGame(game);
	}


	@GetMapping("/{id}")
	public Game findGame(@PathVariable long id) {
		
		//problem1: what if id is not long?
		logger.info("----------------------> GameController /{id}: findGame, id:" + id);
		Game game = gameService.findById(id);
		logger.info("----------------------> GameController /{id}: findGame, game:" + game);
		return game;
	}
	@PutMapping("")
	public Game updateGame(@RequestBody Game game) {
		// problem1: what if body is null or empty?
		// problem2: what if user has non-existant properties?
		return gameService.updateGame(game);
	}
	
	@DeleteMapping("/{id}")
	public void deleteGame(@PathVariable long id) {
		//problem1: what if id is not long?  
		//console: [org.springframework.web.method.annotation.MethodArgumentTypeMismatchException:
		//Postman: "status": 400, "error": "Bad Request",
		gameService.deleteById(id); 
	}
	
	@ExceptionHandler(GameNotFoundException.class)
	@ResponseStatus(value= HttpStatus.BAD_REQUEST) // 400
	public void handleGameNotFoundException() {
		
	}
	// catches all exceptions but not those handled by us
	@ExceptionHandler(Exception.class)
	@ResponseStatus(value= HttpStatus.INTERNAL_SERVER_ERROR) //500
	public void handleOtherExceptions() {
		
	}
}
