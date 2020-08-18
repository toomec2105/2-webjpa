package com.tomek.web_jpa_2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.tomek.web_jpa_2.user.User;

@SpringBootApplication
public class Application {
	private static Logger logger = LoggerFactory.getLogger(Application.class);
	
	public static void main(String[] args) {
		logger.info("----------------------> Starting Spring"); 
		SpringApplication.run(Application.class, args);
		logger.info("----------------------> Ending Spring's run()"); 
		
		/*
		 * User user = new User(); User user2 = new User(1L,"","","","");
		 */
		
	}

}
