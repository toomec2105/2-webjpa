package com.tomek.web_jpa_2.user;

import org.springframework.stereotype.Component;

@Component
public class UserHelpService {
	public String getHelp(String topic) {
		String output = "";
		switch (topic) {
		case "signup":
			output = "Here is our help on signup.";
			break;
		case "login":
			output = "Here is our help on login.";
			break;
		case "installation":
			output = "Here is our help on installation.";
			break;
		default:
			output = "No help provided.";
			break;
		}
		return output;

	}

	public String sendEmail(String email) {
		return "Email has been sent to: " + email;
	}
}
