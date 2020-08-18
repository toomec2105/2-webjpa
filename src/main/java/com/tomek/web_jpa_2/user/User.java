package com.tomek.web_jpa_2.user;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
@Entity
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String email;
	private String password;
	private String role;
	private String username;

	public User(String email, String password, String role, String username) {
		super();
		this.email = email;
		this.password = password;
		this.role = role;
		this.username = username;
	}

	public User(Long id, String username) {
		super();
		this.id = id;
		this.username = username;
	}

}