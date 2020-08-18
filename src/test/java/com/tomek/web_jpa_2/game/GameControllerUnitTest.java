package com.tomek.web_jpa_2.game;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;



@WebMvcTest(GameController.class)
public class GameControllerUnitTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private GameService gameService;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Test
	public void whenGETall_returnsListOfGames() throws Exception{
		//arrange
		Game game1 = new Game(1L,"Leauge of Legends","MOBA");
		Game game2 = new Game(2L,"Hades","Rougelike");
		List<Game> games = new ArrayList<>();
		games.add(game1);
		games.add(game2);

		org.mockito.Mockito.when(gameService.findGames()).thenReturn(games);
		
		//act
		mockMvc.perform(
				get("/games")
		)
		
		//assert
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(content().string(containsString("Leauge of Legends")))
		.andExpect(jsonPath("@.[1].id").value(2L))
		.andExpect(jsonPath("@.[0].id").isNumber())
		.andExpect(jsonPath("@.[0].category").isNotEmpty())
		.andExpect(jsonPath("@.[0].name").isString())
		.andExpect(jsonPath("@.[1].name").value("Hades"))
		.andExpect(jsonPath("@.[1].category").value("Rougelike"));
	}
	
	@Test
	public void whenGETall_returnsListOfGames2() throws Exception{	
		//arrange
		org.mockito.Mockito.when(gameService.findGames()).thenReturn(Collections.emptyList());
			
		//act
		mockMvc.perform(
				get("/games")	
		)
		
		//assert
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("$").exists());
	}
	
	@Test
	public void givenValidId_whenGETone_returnGame() throws Exception{
		//arrange
		Game game = new Game(1L,"Leauge of Legends","MOBA");
		String gameJson = objectMapper.writeValueAsString(game);
		
		org.mockito.Mockito.when(gameService.findById(1L))
			.thenReturn(game);
					
		//act
		mockMvc.perform(
				get("/games/1")
				.characterEncoding("utf-8") 
		)
		
		//assert
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(content().string(gameJson));
	}
	
	@Test
	public void givenGame_whenPOST_returnsSavedGame() throws Exception{
		//arrange
		Game game = new Game("Leauge of Legends","MOBA");
		String clientGameAsJson = objectMapper.writeValueAsString(game);
		
		Game savedGame = new Game(1L,"Leauge of Legends","MOBA");
		String savedGameAsJson = objectMapper.writeValueAsString(savedGame);
		
		org.mockito.Mockito.when(gameService.saveGame(game))
			.thenReturn(savedGame);
					
		//act
		mockMvc.perform(
				post("/games")
				.characterEncoding("utf-8") 
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(clientGameAsJson)
		)
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("$").exists())
		.andExpect(content().string(savedGameAsJson));
	}
	
	@Test
	public void givenGame_whenPUT_returnsUpdatedGame() throws Exception{
		//arrange
		Game game = new Game(1L,"Leauge of Legends","MOBA");
		String gameAsJson = objectMapper.writeValueAsString(game);
		
		org.mockito.Mockito.when(gameService.updateGame(game))
			.thenReturn(game);
					
		//act
		mockMvc.perform(
				put("/games")
				.characterEncoding("utf-8") 
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(gameAsJson)
		)
		
		//assert
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("$").exists())
		.andExpect(content().string(gameAsJson));
	}
	
	@Test
	public void givenInvalidId_whenGETone_returns400() throws Exception{
		//arrange			
		long id = 132L;
		doThrow(new GameNotFoundException()).when(gameService).findById(id);
		
		//act
		mockMvc.perform(
				get("/games/" + id)
				.characterEncoding("utf-8") 				
		)
		
		//assert
		.andDo(print())
		.andExpect(status().is(400))
		.andExpect(status().isBadRequest());
	}
	
	@Test
	public void givenValidId_whenDelete_returns200() throws Exception{
		//arrange			
		long id = 2L;
		doNothing().when(gameService).deleteById(id);// when void return type
					
		//act
		mockMvc.perform(
				delete("/games/" + id)
				.characterEncoding("utf-8") 
		)
		
		//assert
		.andDo(print())
		.andExpect(status().is(200));
	}
	
	@Test
	public void givenInvalidId_whenDelete_returns400() throws Exception{
		//arrange			
		long id = 132L;
		doThrow(new GameNotFoundException()).when(gameService).deleteById(id);
		
		//act
		mockMvc.perform(
				delete("/games/" + id)
				.characterEncoding("utf-8") 
		)
		
		//assert
		.andDo(print())
		.andExpect(status().is(400))
		.andExpect(status().isBadRequest());
	}
	
	@Test
	public void givenGame_whenPOST_returnsSavedGame_2_usingMvcResult() throws Exception{
		//arrange
		Game game = new Game("Leauge of Legends","MOBA");
		String clientGameAsJson = objectMapper.writeValueAsString(game);
		
		Game savedGame = new Game(1L,"Leauge of Legends","MOBA");
		String savedGameAsJson = objectMapper.writeValueAsString(savedGame);
		
		org.mockito.Mockito.when(gameService.saveGame(game))
			.thenReturn(savedGame);
					
		//act
		MvcResult result = mockMvc.perform(
				post("/games")
				.characterEncoding("utf-8") 
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(clientGameAsJson)
		).andReturn();
		
		//assert
		System.out.println(result.getHandler());
		MockHttpServletResponse response =  result.getResponse();
		String body = response.getContentAsString();
		assertEquals(savedGameAsJson, body);
		System.out.println(response.getContentType());
		assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());
		assertEquals("application/json",response.getHeader("Content-Type"));
	}
	@Test
	public void givenValidId_whenGETone_returnGame_2_withMvcResult() throws Exception{
		//arrange
		Game game = new Game(2L,"Leauge of Legends","MOBA");
		
		org.mockito.Mockito.when(gameService.findById(2L))
			.thenReturn(game);
					
		//act
		MvcResult result = mockMvc.perform(
				get("/games/2")
				.characterEncoding("utf-8") 
		).andReturn();
		
		MockHttpServletResponse response =  result.getResponse();
		String gameAsJsonString = response.getContentAsString();
		Game savedGame = objectMapper.readValue(gameAsJsonString, Game.class);
		assertEquals(game, savedGame);
		assertEquals(game.getId(), savedGame.getId());
		assertEquals(game.getCategory(), savedGame.getCategory());
	}
}
