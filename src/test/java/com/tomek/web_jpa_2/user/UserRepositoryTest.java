package com.tomek.web_jpa_2.user;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.tomek.web_jpa_2.user.User;
import com.tomek.web_jpa_2.user.UserRepository;

@Tag("db")
@DataJpaTest
public class UserRepositoryTest {

	@Autowired
	private TestEntityManager testEntityManager;

	@Autowired
	private UserRepository userRepository;

	@BeforeEach
	void setup() {
		clearRepo();
	}

	void clearRepo() {
		userRepository.deleteAll();
		org.junit.jupiter.api.Assumptions.assumeTrue(userRepository.count() == 0);
	}

	@Test
	void testMappings() {
		User user = new User("jeszcze@interia.pl", "polandia43", "ADMIN", "Czerwony6");
		User found = testEntityManager.persistFlushFind(user);
		/*
		 * assertEquals(user.getId(), found.getId()); assertEquals(user.getEmail(),
		 * found.getEmail()); assertEquals(user.getPassword(), found.getPassword());
		 * assertEquals(user.getRole(), found.getRole());
		 * assertEquals(user.getUsername(), found.getUsername());
		 */

		org.assertj.core.api.Assertions.assertThat(user.getId()).isEqualTo(found.getId());
		org.assertj.core.api.Assertions.assertThat(user.getEmail()).isEqualTo(found.getEmail());
		org.assertj.core.api.Assertions.assertThat(user.getPassword()).isEqualTo(found.getPassword());
		org.assertj.core.api.Assertions.assertThat(user.getRole()).isEqualTo(found.getRole());
		org.assertj.core.api.Assertions.assertThat(user.getUsername()).isEqualTo(found.getUsername());
	}

	@Test
	void givenId_deletesUser() {
		// arrange
		User user = new User("jeszcze@interia.pl", "polandia43", "ADMIN", "Czerwony6");
		Long id = testEntityManager.persistAndGetId(user, Long.class);

		// act -> The method under test
		userRepository.deleteById(id);

		// assert
		User deleted = testEntityManager.find(User.class, id);
		
		//assertNull(deleted);
		org.assertj.core.api.Assertions.assertThat(deleted).isNull();
		
	}

	@Test
	void givenEmail_shouldGetUser() {
		User userToFind = new User("jeszczaz@interia.pl", "polandia43", "ADMIN", "Czerwony6");
		Long id = testEntityManager.persistAndGetId(userToFind, Long.class);
		testEntityManager.persist(new User("gzybiaz@onet.pl", "lesnictfo", "USER", "zielyny53"));
		testEntityManager.persist(new User("panek7@onet.pl", "rymcymcym", "ADMIN", "slachtagurom"));

		User found = userRepository.findByEmailAddressNATIVE("jeszczaz@interia.pl");

		
		
		//assertNotNull(found);
		//assertEquals(id, found.getId());
		
		org.assertj.core.api.Assertions.assertThat(found).isNotNull();
		org.assertj.core.api.Assertions.assertThat(found.getId()).isEqualTo(id);
	}

	@Test
	void givenExpression_shouldGetUsersWithMatchingEmail() {
		testEntityManager.persist(new User("jeszczaz@interia.pl", "polandia43", "ADMIN", "Czerwony6"));
		testEntityManager.persist(new User("gzybiaz@onet.pl", "lesnictfo", "USER", "zielyny53"));
		testEntityManager.persist(new User("panek7@onet.pl", "rymcymcym", "ADMIN", "slachtagurom"));

		List<User> found = userRepository.getUsersWithMatchingEmailJPQL("az");

		//assertNotNull(found);
		//assertEquals(2, found.size());
		
		org.assertj.core.api.Assertions.assertThat(found).isNotNull();
		org.assertj.core.api.Assertions.assertThat(found.size()).isEqualTo(2);

	}
}
