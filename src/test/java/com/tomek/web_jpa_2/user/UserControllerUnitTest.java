package com.tomek.web_jpa_2.user;

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


@WebMvcTest(UserController.class)
public class UserControllerUnitTest {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserService userService;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Test
	public void whenGETall_returnsListOfUsers() throws Exception{
		/* Alternative with just one User
		 * User user1 = new User(1L,"john@gmail.com", "john123", "ADMIN", "John Brown");
		 * org.mockito.Mockito.when(userService.findUsers())
		 *       .thenReturn(Collections.singletonList(user1));
		 * 
		 */
		
		//arrange
		User user1 = new User(1L,"john@gmail.com", "john123", "ADMIN", "John Brown");
		User user2 = new User(2L,"alice.white@gmail.com", "alice9999", "USER", "Alice White");
		List<User> users = new ArrayList<>();
		users.add(user1);
		users.add(user2);

		org.mockito.Mockito.when(userService.findUsers()).thenReturn(users);
		
		//act
		mockMvc.perform(
				get("/users/")
		)
		
		//assert
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(content().string(containsString("alice9999")))
		.andExpect(jsonPath("@.[1].id").value(2L))
		.andExpect(jsonPath("@.[0].id").isNumber())
		.andExpect(jsonPath("@.[0].username").isNotEmpty())
		.andExpect(jsonPath("@.[0].password").isString())
		.andExpect(jsonPath("@.[1].email").value("alice.white@gmail.com"))
		.andExpect(jsonPath("@.[1].username").value("Alice White"));
	}
	@Test
	public void whenGETall_returnsListOfUsers2() throws Exception{	
		//arrange
		org.mockito.Mockito.when(userService.findUsers()).thenReturn(Collections.emptyList());
			
		//act
		mockMvc.perform(
				get("/users")	
		)
		
		//assert
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("$").exists());
	}
	
	@Test
	public void givenValidId_whenGETone_returnUser() throws Exception{
		//arrange
		User user = new User (2L,"krzychu@onet.pl", "DerekSzmitd", "ADMIN", "Pawelelek Sagt");
		String userJson = objectMapper.writeValueAsString(user);
		
		org.mockito.Mockito.when(userService.findById(2L))
			.thenReturn(user);
					
		//act
		mockMvc.perform(
				get("/users/2")
				.characterEncoding("utf-8") 
		)
		
		//assert
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(content().string(userJson));
	}
	
	@Test
	public void givenUser_whenPOST_returnsSavedUser() throws Exception{
		//arrange
		User user = new User ("krzychu@onet.pl", "DerekSzmitd", "ADMIN", "Pawelelek Sagt");
		String clientUserAsJson = objectMapper.writeValueAsString(user);
		
		User savedUser = new User (1L,"krzychu@onet.pl", "DerekSzmitd", "ADMIN", "Pawelelek Sagt");
		String savedUserAsJson = objectMapper.writeValueAsString(savedUser);
		
		org.mockito.Mockito.when(userService.saveUser(user))
			.thenReturn(savedUser);
					
		//act
		mockMvc.perform(
				post("/users")
				.characterEncoding("utf-8") 
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(clientUserAsJson)
		)
		
		//assert
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("$").exists())
		.andExpect(content().string(savedUserAsJson));
	}
	
	
	@Test
	public void givenUser_whenPUT_returnsUpdatedUser() throws Exception{
		//arrange
		User user = new User (1L,"krzychu@onet.pl", "DerekSzmitd", "ADMIN", "Pawelelek Sagt");
		String userAsJson = objectMapper.writeValueAsString(user);
		
		org.mockito.Mockito.when(userService.updateUser(user))
			.thenReturn(user);
					
		//act
		mockMvc.perform(
				put("/users")
				.characterEncoding("utf-8") 
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(userAsJson)
		)
		
		//assert
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("$").exists())
		.andExpect(content().string(userAsJson));
	}
	
	@Test
	public void givenInvalidId_whenGETone_returns400() throws Exception{
		//arrange			
		long id = 132L;
		doThrow(new UserNotFoundException()).when(userService).findById(id);
		
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
		doNothing().when(userService).deleteById(id);// when void return type
					
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
		doThrow(new UserNotFoundException()).when(userService).deleteById(id);
		
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
	//-----------------------------------------------------
	@Test
	public void givenUser_whenPOST_returnsSavedUser_2_usingMvcResult() throws Exception{
		//arrange
		User user = new User ("krzychu@onet.pl", "DerekSzmitd", "ADMIN", "Pawelelek Sagt");
		String clientUserAsJson = objectMapper.writeValueAsString(user);
		
		User savedUser = new User (1L,"krzychu@onet.pl", "DerekSzmitd", "ADMIN", "Pawelelek Sagt");
		String savedUserAsJson = objectMapper.writeValueAsString(savedUser);
		
		org.mockito.Mockito.when(userService.saveUser(user))
			.thenReturn(savedUser);
					
		//act
		MvcResult result = mockMvc.perform(
				post("/users")
				.characterEncoding("utf-8") 
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(clientUserAsJson)
		).andReturn();
		
		//assert
		// using MockHttpServletResponse response we can have direct access to the response by 
		// result.getResponse() and we can make assertions just like we did with 
		// .andExpect(...) 
		System.out.println(result.getHandler()); // com.tomek.web_jpa_2.user.UserController#addUser(User)
		MockHttpServletResponse response =  result.getResponse();
		String body = response.getContentAsString();
		assertEquals(savedUserAsJson, body); // .andExpect(content().string(savedUserAsJson))
		System.out.println(response.getContentType());
		assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());
		assertEquals("application/json",response.getHeader("Content-Type"));
		
		/*.andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("$").exists())
		.andExpect(content().string(savedUserAsJson));*/	
		
	}
	
	@Test
	public void givenValidId_whenGETone_returnUser_2_withMvcResult() throws Exception{
		//arrange
		User user = new User (2L,"krzychu@onet.pl", "DerekSzmitd", "ADMIN", "Pawelelek Sagt");
		
		org.mockito.Mockito.when(userService.findById(2L))
			.thenReturn(user);
					
		//act
		MvcResult result = mockMvc.perform(
				get("/users/2")
				.characterEncoding("utf-8") 
		).andReturn();
		
		MockHttpServletResponse response =  result.getResponse();
		String userAsJsonString = response.getContentAsString();
		User savedUser = objectMapper.readValue(userAsJsonString, User.class);
		assertEquals(user, savedUser);
		assertEquals(user.getId(), savedUser.getId());
		assertEquals(user.getPassword(), savedUser.getPassword());
		//assert
		/*.andDo(print())
		.andExpect(status().isOk())
		.andExpect(content().string(userJson));*/
	}
}
