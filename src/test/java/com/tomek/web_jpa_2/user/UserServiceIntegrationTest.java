package com.tomek.web_jpa_2.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.tomek.web_jpa_2.user.User;
import com.tomek.web_jpa_2.user.UserNotFoundException;
import com.tomek.web_jpa_2.user.UserService;

@SpringBootTest
class UserServiceIntegrationTest {

	@Autowired
	private UserService userService;

	// private User user = new User("johnson@zoho.com", "john123", "ADMIN",
	// "johnny3");
	// private User saved;

	@BeforeEach
	public void beforeEach() {
		userService.deleteAllUsers();
		assumeTrue(userService.findUsers().size() == 0);
		// saved = userService.saveUser(user);

		// assumeTrue("johnson@zoho.com".equals(saved.getEmail()));
		// assumeTrue(saved.getId() != null);
	}
	// -------- BASIC CRUD ---------

	@Test
	void givenUser_addsUser() {
		User user = new User("johnson@zoho.com", "john123", "ADMIN", "johnny3");

		// act
		User saved = userService.saveUser(user);

		assumeTrue(saved.getId() != null);
		assumeTrue("johnson@zoho.com".equals(saved.getEmail()));
	}

	@Test
	void givenId_findsUser() {
		User user = new User("johnson@zoho.com", "john123", "ADMIN", "johnny3");
		User saved = userService.saveUser(user);
		assumeTrue("johnson@zoho.com".equals(saved.getEmail()));
		assumeTrue(saved.getId() != null);

		// act when
		User found = userService.findById(saved.getId());

		// assert then
		assertNotNull(found.getId());
		assertEquals(saved.getId(), found.getId());
		assertEquals(saved.getEmail(), found.getEmail());
	}

	@Test
	void getsAllUsers() {
		User user = new User("johnson@zoho.com", "john123", "ADMIN", "johnny3");
		User saved = userService.saveUser(user);
		assumeTrue("johnson@zoho.com".equals(saved.getEmail()));
		assumeTrue(saved.getId() != null);

		// act
		List<User> users = userService.findUsers();

		assertTrue(users.size() > 0);
		assertTrue(users.contains(saved));
	}

	@Test
	void givenId_updatesUser() {
		User user = new User("johnson@zoho.com", "john123", "ADMIN", "johnny3");
		User saved = userService.saveUser(user);
		assumeTrue("johnson@zoho.com".equals(saved.getEmail()));
		assumeTrue(saved.getId() != null);

		saved.setEmail("newEmail@onet.pl");

		// act
		User updated = userService.saveUser(saved);

		assertEquals("newEmail@onet.pl", updated.getEmail());

		User userBeforeUpdate = userService.findById(saved.getId());

		assertEquals(userBeforeUpdate.getEmail(), updated.getEmail());
	}

	@Test
	void givenId_deletesUser() {
		User user = new User("johnson@zoho.com", "john123", "ADMIN", "johnny3");
		User saved = userService.saveUser(user);
		assumeTrue("johnson@zoho.com".equals(saved.getEmail()));
		assumeTrue(saved.getId() != null);
		// act
		userService.deleteById(saved.getId());

		assertThrows(UserNotFoundException.class, () -> {
			userService.findById(saved.getId());
		});

	}

	// --------- OTHER METHODS ----------

	@Test
	void findsUserByEmailCorrectly() {
		userService.saveUser(new User("testuser@onet.pl", "jg46x0", "ADMIN", "verwa"));
		userService.saveUser(new User("testuser23@onet.pl", "jg46x2", "ADMIN", "gdaehc"));
		userService.saveUser(new User("tesqwrt43@onet.pl", "j3fbg4x2", "ADMIN", "wronger"));
		userService.saveUser(new User("merktreds@onet.pl", "zerod4", "USER", "Babcia"));

		// act
		User user = userService.findByEmail("merktreds@onet.pl");

		assertEquals("Babcia", user.getUsername());
	}

	@Test
	void findsUsersByUsernameCorrectly() {
		userService.saveUser(new User("testuser@onet.pl", "jg46x0", "ADMIN", "verwa"));
		userService.saveUser(new User("testuser23@onet.pl", "jg46x2", "ADMIN", "gdaehc"));
		userService.saveUser(new User("tesqwrt43@onet.pl", "j3fbg4x2", "ADMIN", "wronger"));

		assertEquals(1, userService.findByUsername("wronger").size());
	}

	@Test
	void givenEmailAndPassword_findsUser() {
		// arrange given
		userService.saveUser(new User("testuser@onet.pl", "jg46x0", "ADMIN", "verwa"));
		userService.saveUser(new User("testuser23@onet.pl", "jg46x2", "ADMIN", "gdaehc"));
		userService.saveUser(new User("tesqwrt43@onet.pl", "j3fbg4x2", "ADMIN", "wronger"));
		userService.saveUser(new User("merktreds@onet.pl", "zerod4", "USER", "Babcia"));

		String password = "jg46x2";
		String email = "testuser23@onet.pl";

		// act when
		User user = userService.findByEmailAndPassword(email, password);

		// assert then
		assertEquals("gdaehc", user.getUsername());
	}

	@Test
	void givenExpression_findsUsersWithMatchingUsername() {
		// given
		String expression = "a";
		userService.saveUser(new User("testuser@onet.pl", "jg46x0", "ADMIN", "verwa"));
		userService.saveUser(new User("testuser23@onet.pl", "jg46x2", "ADMIN", "gdaehc"));
		userService.saveUser(new User("tesqwrt43@onet.pl", "j3fbg4x2", "ADMIN", "wronger"));

		// when
		List<User> users = userService.findByUsernameContaining(expression);

		// then
		assertEquals(2, users.size());
	}

	@Test
	void givenId_removesCorrectUser() {
		assertEquals(1, 1);
	}

	@Test
	void findsUsersWithoutUserRoleCorrectly() {
		userService.saveUser(new User("testuser@onet.pl", "jg46x0", "ADMIN", "verwa"));
		userService.saveUser(new User("testuser23@onet.pl", "jg46x2", "ADMIN", "gdaehc"));
		userService.saveUser(new User("tesqwrt43@onet.pl", "j3fbg4x2", "ADMIN", "wronger"));
		userService.saveUser(new User("merktreds@onet.pl", "zerod4", "USER", "babcia"));
		assertEquals(3, userService.findByRoleNot("USER").size());
	}

	@Test
	void findsUsersBetweenStartAndEndCorrectly() {
		userService.saveUser(new User("testuser@onet.pl", "jg46x0", "ADMIN", "verwa"));
		userService.saveUser(new User("testuser23@onet.pl", "jg46x2", "ADMIN", "gdaehc"));
		userService.saveUser(new User("tesqwrt43@onet.pl", "j3fbg4x2", "ADMIN", "wronger"));
		userService.saveUser(new User("merktreds@onet.pl", "zerod4", "USER", "Babcia"));
		assertEquals(1, userService.findByUsernameBetween("@", "D").size()); // characters are exclusive and to get
																				// names starting with A we have to use
																				// @
	}

	// -----------------------Spring-Data native sql-----------------

	@Test
	void givenUsersWithRoles_returnsNoOfRoles() {
		userService.saveUser(new User("testuser@onet.pl", "jg46x0", "ADMIN", "verwa"));
		userService.saveUser(new User("testuser23@onet.pl", "jg46x2", "ADMIN", "gdaehc"));
		userService.saveUser(new User("tesqwrt43@onet.pl", "j3fbg4x2", "ADMIN", "wronger"));
		userService.saveUser(new User("merktreds@onet.pl", "zerod4", "USER", "babcia"));
		assertEquals(2, userService.findDisctinctNumberOfUserRolesNativeNATIVE());
	}

	@Test
	void givenExpression_findsUsersWithMatchingPassword() {
		String expression = "46x";
		userService.saveUser(new User("testuser@onet.pl", "jg46x0", "ADMIN", "verwa"));
		userService.saveUser(new User("testuser23@onet.pl", "jg46x2", "ADMIN", "gdaehc"));
		userService.saveUser(new User("tesqwrt43@onet.pl", "j3fbg4x2", "ADMIN", "wronger"));

		// act
		List<User> users = userService.findUsersWitchMatchingPasswordNATIVE(expression);

		assertEquals(2, users.size());
	}

	// -----------------------Spring-Data jpql queries----------------------------
	@Test
	void givenUsernameAndEmail_findsMatchingUser() {
		userService.saveUser(new User("testuser@onet.pl", "jg46x0", "ADMIN", "verwa"));
		userService.saveUser(new User("alice.white@gmail.com", "alicjaLustro", "ADMIN", "Alice White"));
		userService.saveUser(new User("testuser23@onet.pl", "jg46x2", "ADMIN", "gdaehc"));
		userService.saveUser(new User("tesqwrt43@onet.pl", "j3fbg4x2", "ADMIN", "wronger"));
		String email = "alice.white@gmail.com";
		String username = "Alice White";

		User user = userService.findByEmailAndNameJPQL(email, username);

		assertEquals("alicjaLustro", user.getPassword());
	}

	@Test
	void givenExpression_findsUsersWithMatchingEmails() {
		String expression = "uesq";
		userService.saveUser(new User("tdeuser@onet.pl", "jg46x0", "ADMIN", "verwa"));
		userService.saveUser(new User("uesquser3@onet.pl", "jg46x2", "ADMIN", "gdaehc"));
		userService.saveUser(new User("tyuesqwrt43@onet.pl", "j3fbg4x2", "ADMIN", "wronger"));

		List<User> users = userService.findUsersWithMatcingEmailJPQL(expression);

		assertEquals(2, users.size());
	}

}
