package com.tomek.web_jpa_2.user;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	User findByEmail(String email);

	List<User> findByUsername(String username);

	User findByEmailAndPassword(String email, String password);

	List<User> findByUsernameContaining(String searchValue);

	List<User> findByUsernameBetween(String start, String end);

	List<User> findByRoleNot(String role);
	// -------------------------Spring-Data native sql-----------------------------

	@Query(value = "SELECT COUNT(DISTINCT role) FROM users;", nativeQuery = true)
	int getDisctinctNumberOfUserRolesNATIVE();

	@Query(value = "SELECT * FROM users WHERE EMAIL = ?1", nativeQuery = true)
	User findByEmailAddressNATIVE(String email);

	// With parameters don't use semicolon at the end of the query!!!
	@Query(value = "SELECT * FROM users WHERE password LIKE %?1%", nativeQuery = true)
	List<User> findByPasswordNATIVE(String expression);

	// -----------------------Spring-Data jpql queries----------------------------

	// in JPQL you have to use the Java name of the entity class;
	// or @Entity(name ="users")
	@Query(value = "SELECT u from User u WHERE u.email = ?1 AND u.username = ?2")
	User getByEmailAndNameJPQL(String email, String username);

	@Query(value = "SELECT u from User u WHERE u.email LIKE %?1%")
	List<User> getUsersWithMatchingEmailJPQL(String expression);

}
