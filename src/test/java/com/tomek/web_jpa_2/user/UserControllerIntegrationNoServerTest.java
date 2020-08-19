package com.tomek.web_jpa_2.user;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.hasSize;
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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserControllerIntegrationNoServerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;
	
	private User user1;
	private User user2;
	private String user1AsJson;
	private String user2AsJson;

	@BeforeEach
	public void setup() {
		user1 = new User("apKaisa5@gmail.com", "purpleStack", "ADMIN", "Kaisa Stronks");
		user2 = new User("YassQRAQAE@gmail.com", "Ctr7QAQAQAA", "USER", "Yasuo Wind");
		user1AsJson = asJsonString(user1);
		user2AsJson = asJsonString(user2);
		addUser(user1AsJson);
		addUser(user2AsJson);
	}

	@Test
	public void whenGETall_returnsListOfUsers() throws Exception {
		
		//arrange
		int numbersOfUsersAddedFromFile = 7;

		// act
		mockMvc.perform(get("/users"))

				// assert
				.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$").exists())
				.andExpect(jsonPath("$", hasSize(numbersOfUsersAddedFromFile + 2)))
				.andExpect(MockMvcResultMatchers.content().string(containsString("Kaisa Stronks")))
				.andExpect(MockMvcResultMatchers.content().string(containsString("Yasuo Wind")));
	}

	@Test
	public void whenGETall_returnsListOfUsers2() throws Exception {

		// act
		mockMvc.perform(get("/users"))

				// assert
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").exists());
	}

	@Test
	public void givenValidId_whenGETone_returnsTheSameUserConsistently() throws Exception {

		// act
		MvcResult result1a = mockMvc.perform(get("/users/1").characterEncoding("utf-8")).andReturn();
		MvcResult result2a = mockMvc.perform(get("/users/2").characterEncoding("utf-8")).andReturn();
		
		MvcResult result1b = mockMvc.perform(get("/users/1").characterEncoding("utf-8")).andReturn();
		MvcResult result2b = mockMvc.perform(get("/users/2").characterEncoding("utf-8")).andReturn();
		
		// assert
		MockHttpServletResponse response1a =  result1a.getResponse();
		String user1AsJsonStringa = response1a.getContentAsString();
		User savedUser1a = objectMapper.readValue(user1AsJsonStringa, User.class);
			
		MockHttpServletResponse response2a =  result2a.getResponse();
		String user2AsJsonStringa = response2a.getContentAsString();
		User savedUser2a = objectMapper.readValue(user2AsJsonStringa, User.class);
		
		MockHttpServletResponse response1b =  result1b.getResponse();
		String user1AsJsonStringb = response1b.getContentAsString();
		User savedUser1b = objectMapper.readValue(user1AsJsonStringb, User.class);
			
		MockHttpServletResponse response2b =  result2b.getResponse();
		String user2AsJsonStringb = response2b.getContentAsString();
		User savedUser2b = objectMapper.readValue(user2AsJsonStringb, User.class);
		
		assertEquals(savedUser1a, savedUser1b);
		assertEquals(savedUser2a, savedUser2b);
	}
	
	@Test
	public void givenUser_whenPUT_returnsUpdatedUser() throws Exception{
		//arrange
		User newUser = new User(1L,"LeaugeOfDraven@gmail.com", "Ctr7QAQAQAA", "USER", "Yasuo Wind");
		String newUserAsJson = asJsonString(newUser);
		
		//act
		mockMvc.perform(
				put("/users")
				.characterEncoding("utf-8") 
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(newUserAsJson)
		)
		
		//assert
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("$").exists())
		.andExpect(content().string(newUserAsJson));
	}
	
	@Test
	public void givenInvalidId_whenGETone_returns400() throws Exception{
		//arrange			
		long id = 132L;
		
		//act
		mockMvc.perform(
				get("/users/" + id)
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
					
		//act
		mockMvc.perform(
				delete("/users/" + id)
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
		
		//act
		mockMvc.perform(
				delete("/users/" + id)
				.characterEncoding("utf-8") 
		)
		
		//assert
		.andDo(print())
		.andExpect(status().is(400))
		.andExpect(status().isBadRequest());
	}
	
	@Test
	public void givenValidId_whenGETone_returnsTheSameUserConsistently_2_withMvcResult() throws Exception{
		
		//act
		MvcResult result1 = mockMvc.perform(
				get("/users/1")
				.characterEncoding("utf-8") 
		).andReturn();
		
		MvcResult result2 = mockMvc.perform(
				get("/users/1")
				.characterEncoding("utf-8") 
		).andReturn();
		
		//assert
		MockHttpServletResponse response1 =  result1.getResponse();
		String userAsJsonString1 = response1.getContentAsString();
		User savedUser1 = objectMapper.readValue(userAsJsonString1, User.class);
		
		MockHttpServletResponse response2 =  result2.getResponse();
		String userAsJsonString2 = response2.getContentAsString();
		User savedUser2 = objectMapper.readValue(userAsJsonString2, User.class);

		assertEquals(savedUser1, savedUser2);
		assertEquals(savedUser1.getId(), savedUser2.getId());
		assertEquals(savedUser1.getPassword(), savedUser2.getPassword());
	}
	// ------------------helpers---------------------
	private String asJsonString(User user) {
		try {
			return objectMapper.writeValueAsString(user);
		} catch (JsonProcessingException e) {
			throw new RuntimeException();
		}
	}

	private void addUser(String user) {
		try {
			mockMvc.perform(post("/users")
					.characterEncoding("utf-8")
					.contentType(MediaType.APPLICATION_JSON_VALUE)
					.content(user));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
