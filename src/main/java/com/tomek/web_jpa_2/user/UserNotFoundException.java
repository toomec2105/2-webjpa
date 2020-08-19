package com.tomek.web_jpa_2.user;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UserNotFoundException extends RuntimeException {
	public UserNotFoundException (String message) {
		super(message);
	}
	
}
