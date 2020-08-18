package com.tomek.web_jpa_2.home;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tomek.web_jpa_2.user.HelpResponse;
import com.tomek.web_jpa_2.user.UserController;
import com.tomek.web_jpa_2.user.UserHelpService;

@RestController
@RequestMapping // empty string by default
public class HomeController {
	private static Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserHelpService userHelpService;

	@GetMapping // This will map to localhost:8080 but only if @RequestMapping over class name
				// maps to empty string
	public String showIndex() {
		logger.info("----------------------> Entering /showIndex");
		return "My index page is here";
	}

	@GetMapping("/help") // ?topic=value&email=value
	public HelpResponse help(@RequestParam String topic, @RequestParam(required = false) String email) {
		String helpMessage = userHelpService.getHelp(topic);
		String emailMessage = "No email";

		logger.info("----------------------> Entering /help " + helpMessage);

		if (email != null) {
			emailMessage = userHelpService.sendEmail(email);
			logger.info("----------------------> Entering /help " + emailMessage);
		}

		logger.info("----------------------> topic: " + topic + ", email: " + email);
		HelpResponse helpResponse = new HelpResponse("This is help page. ", helpMessage, emailMessage);
		return helpResponse;
	}

	@GetMapping("/about")
	public String about() {
		logger.info("----------------------> Entering /about");
		return "This page is an example of a web app";
	}

}
