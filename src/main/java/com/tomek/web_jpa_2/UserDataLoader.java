package com.tomek.web_jpa_2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.tomek.web_jpa_2.user.User;
import com.tomek.web_jpa_2.user.UserRepository;


@Component
public class UserDataLoader implements CommandLineRunner {
	
	private static Logger logger = LoggerFactory.getLogger(Application.class);
	
	@Autowired
	private UserRepository userRepository;

	
	@Override
	public void run(String... args) throws Exception {
		logger.info("----------------------> Saving darek user"); 
		User user = new User("darek@gmail.com", "darek666", "ADMIN", "Darek Scott");
		userRepository.save(user);
	}

}
