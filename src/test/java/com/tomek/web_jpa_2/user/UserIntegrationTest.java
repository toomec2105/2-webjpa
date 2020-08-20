package com.tomek.web_jpa_2.user;

import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.assertj.core.api.Assertions;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class UserIntegrationTest {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	private static final String USERS_ENDPOINT = "http://localhost:";

	private User user;
	private User savedUser;
	private Long savedUserId;

	@BeforeEach
	private void setup() {
		user = new User("koles@wodo.com", "pasdaf33", "ADMIN", "PolarZiemo");
		savedUser = restTemplate.postForObject(USERS_ENDPOINT + port + "/users", user, User.class);
		savedUserId = savedUser.getId();
	}

	@Test
	public void whenGivenValidId_GET_returnsUser() throws Exception {
		// act
		User foundUser = restTemplate.getForObject(USERS_ENDPOINT + port + "/users/" + savedUserId, User.class);

		// assert
		assertEquals(foundUser.getUsername(), user.getUsername());
	}

	@Test
	public void whenGivenValidId_GET_returnsUser_withRequestEntity() throws Exception {
		// act
		ResponseEntity<User> responseEntity = restTemplate.getForEntity(USERS_ENDPOINT + port + "/users/" + savedUserId,
				User.class);

		// assert
		User foundUser = responseEntity.getBody();

		assertEquals(200, responseEntity.getStatusCodeValue());
		assertEquals(foundUser.getUsername(), user.getUsername());
	}

	@Test
	public void whenGivenUser_POST_returnsSavedUser() throws Exception {
		// arrange
		User newUser = new User("posrtf34@onet.com", "riczkWarsaz", "ADMIN", "Username85");

		// act
		User found = restTemplate.postForObject(USERS_ENDPOINT + port + "/users", newUser, User.class);

		// assert
		assertNotNull(found);
		assertNotNull(found.getId());
		assertEquals(newUser.getUsername(), found.getUsername());
	}

	@Test
	public void whenGivenUser_PUT_returnsUpdatedUser() throws Exception {
		// arrange
		User newUser = new User(savedUserId, "koles@wodo.com", "pasdaf33", "ADMIN", "Ziemeczkox");

		// act
		restTemplate.put(USERS_ENDPOINT + port + "/users", newUser, User.class);

		// assert
		ResponseEntity<User> responseEntity = restTemplate.getForEntity(USERS_ENDPOINT + port + "/users/" + savedUserId,
				User.class);
		User found = responseEntity.getBody();

		assertNotNull(found);
		assertNotNull(found.getId());
		assertNotEquals(user.getUsername(), found.getUsername());
		assertEquals(newUser.getUsername(), found.getUsername());
	}

	@Test
	public void GET_returnsUsers_withRequestEntity() throws Exception {
		// arrange
		User secondUser = new User("truskMe@skype.de", "bramkaszCfel", "USER", "footBALLgod");
		restTemplate.postForObject(USERS_ENDPOINT + port + "/users", secondUser, User.class);

		// act
		ResponseEntity<User[]> responseEntity = restTemplate.getForEntity(USERS_ENDPOINT + port + "/users/",
				User[].class);

		// assert User foundUser = responseEntity.getBody();
		List<Object> users = Arrays.asList(responseEntity.getBody());

		assertEquals(200, responseEntity.getStatusCodeValue());
		assertNotNull(users);
		assertTrue(users.size() >= 2);

		User[] usersArr = responseEntity.getBody();
		List<User> list = Stream.of(usersArr).collect(Collectors.toList());

		assertNotNull(list);
		assertTrue(list.size() >= 2);

		Assertions.assertThat(list).extracting("username").contains("footBALLgod", "PolarZiemo");

		Assertions.assertThat(list).extracting("email", "role", "username").contains(
				tuple("truskMe@skype.de", "USER", "footBALLgod"), tuple("koles@wodo.com", "ADMIN", "PolarZiemo"));
	}

	@Test
	public void whenGivenValidId_DELETE_deletesUser() throws Exception {
		// arrange

		ResponseEntity<User[]> responseEntity = restTemplate.getForEntity(USERS_ENDPOINT + port + "/users/",
				User[].class);

		User[] initialUsers = responseEntity.getBody();
		int numberOfUsersBeforeDelete = initialUsers.length;
		// act
		restTemplate.delete(USERS_ENDPOINT + port + "/users/" + savedUserId);

		// assert
		ResponseEntity<User[]> responseEntity2 = restTemplate.getForEntity(USERS_ENDPOINT + port + "/users/",
				User[].class);
		User[] usersArr = responseEntity2.getBody();
		List<User> listAfterDelete = Stream.of(usersArr).collect(Collectors.toList());

		Assertions.assertThat(listAfterDelete).extracting("username").doesNotContain(user.getUsername());
		assertEquals(listAfterDelete.size(), numberOfUsersBeforeDelete - 1);

	}

}
