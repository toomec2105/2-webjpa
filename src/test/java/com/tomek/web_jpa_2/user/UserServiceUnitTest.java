package com.tomek.web_jpa_2.user;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/*import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
*/
//@ExtendWith(MockitoExtension.class)
public class UserServiceUnitTest {

	/*
	 * @Mock private UserRepository userRepository;
	 * 
	 * @InjectMocks private UserService userService;
	 */

	private UserRepository userRepository = org.mockito.Mockito.mock(UserRepository.class);

	private UserService userService = new UserService(userRepository);

	@BeforeEach
	void setup() {

	}

	// -----------Checking Return Types---------------

	@Test
	public void givenEmail_whenFindByEmail_returnsCorrectUser() {

		// arrange
		User user = new User(1L, "jeszcze@interia.pl", "polandia43", "ADMIN", "Czerwony6");
		org.mockito.Mockito.when(userRepository.findByEmail("jeszcze@interia.pl")).thenReturn(user);

		// act
		User found = userService.findByEmail("jeszcze@interia.pl");

		// assert
		org.assertj.core.api.Assertions.assertThat(found).isNotNull();
		org.assertj.core.api.Assertions.assertThat(found.getId()).isEqualTo(1L);
		org.assertj.core.api.Assertions.assertThat(found.getEmail()).isEqualTo("jeszcze@interia.pl");
	}

	@Test
	public void givenExpression_whenFindByUsernameContaining_returnsMatchingUsers() {

		// arrange
		User user1 = new User(1L, "jeszcze@interia.pl", "polandia43", "ADMIN", "Czerwonyt6");
		User user2 = new User(2L, "ciernobyl3@interia.pl", "wladyslav", "USER", "ParaPetkayt");
		List<User> users = new ArrayList<>();
		users.add(user1);
		users.add(user2);
		org.mockito.Mockito.when(userRepository.findByUsernameContaining("yt")).thenReturn(users);

		// act
		List<User> userList = userService.findByUsernameContaining("yt");

		// assert
		org.assertj.core.api.Assertions.assertThat(userList).isNotNull();
		org.assertj.core.api.Assertions.assertThat(userList.size()).isEqualTo(2);
	}

	@Test
	public void givenExpression_whenFindByUsernameContaining_returnsMatchingUsers_simplified() {

		// arrange
		org.mockito.Mockito.when(userRepository.findByUsernameContaining("yt"))
				.thenReturn(org.mockito.ArgumentMatchers.<User>anyList()); // !!!

		// act
		List<User> returnedUsers = userService.findByUsernameContaining("yt");

		// assert
		org.assertj.core.api.Assertions.assertThat(returnedUsers).isNotNull();

		// --------------------------

		// arrange
		org.mockito.Mockito.when(userRepository.findByUsernameContaining(org.mockito.ArgumentMatchers.anyString()))
				.thenReturn(org.mockito.ArgumentMatchers.<User>anyList()); // !!!

		// act
		List<User> foundUsers = userService.findByUsernameContaining(""); // you can't use a matcher here

		// assert
		org.assertj.core.api.Assertions.assertThat(foundUsers).isNotNull();
	}

	@Test
	public void givenEmail_whenFindByEmailAddressNATIVE_returnsUser() {

		// arrange
		org.mockito.Mockito.when(userRepository.findByEmailAddressNATIVE("krowjan@onet.pl")).thenReturn(new User());

		// act
		User found = userService.findByEmailAddressNATIVE("krowjan@onet.pl");

		// assert
		org.assertj.core.api.Assertions.assertThat(found).isInstanceOf(User.class);
	}

	public void givenPassword_whenFindByPasswordNATIVE_returnsUsers() {
		org.mockito.Mockito.when(userRepository.findByPasswordNATIVE("pass"))
				.thenReturn(org.mockito.ArgumentMatchers.<User>anyList()); // !!!

// act
		List<User> foundUsers = userService.findByUsernameContaining("pass"); // you can't use a matcher here

// assert
		org.assertj.core.api.Assertions.assertThat(foundUsers).isNotNull();
	}
}
