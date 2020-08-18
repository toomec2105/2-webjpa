package com.tomek.web_jpa_2.user;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerIntegrationNoServerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	private String user1;
	private String user2;

	@BeforeEach
	public void setup() {
		user1 = asJsonString(new User("apKaisa5@gmail.com", "purpleStack", "ADMIN", "Kaisa Stronks"));
		user2 = asJsonString(new User("YassQRAQAE@gmail.com", "Ctr7QAQAQAA", "USER", "Yasuo Wind"));

		addUser(user1);
		addUser(user2);
	}

	@Test
	public void whenGETall_returnsListOfUsers() throws Exception {

		int numbersOfUsersAddedFromFile = 7;

		// act
		mockMvc.perform(get("/users/all"))

				// assert
				.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$").exists())
				.andExpect(jsonPath("$", hasSize(numbersOfUsersAddedFromFile + 2)))
				.andExpect(MockMvcResultMatchers.content().string(containsString("Kaisa Stronks")))
				.andExpect(MockMvcResultMatchers.content().string(containsString("Yasuo Wind")));
	}

	private String asJsonString(User user) {
		try {
			return objectMapper.writeValueAsString(user);
		} catch (JsonProcessingException e) {
			throw new RuntimeException();
		}

	}

	private void addUser(String user) {
		try {
			mockMvc.perform(post("/users/add").characterEncoding("utf-8").contentType(MediaType.APPLICATION_JSON_VALUE)
					.content(user));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(user);
	}

}
