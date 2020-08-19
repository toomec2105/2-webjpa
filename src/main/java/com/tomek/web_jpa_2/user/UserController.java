package com.tomek.web_jpa_2.user;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController // = @Controller @ResponseBody
@RequestMapping("users") // modifies the path for each method in this class
//@RequestMapping maps all http methods
public class UserController {
	private static Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	@GetMapping(path = "")
	public List<User> showAllUsers() {
		logger.info("----------------------> Entering /all");
		// if list.size()=0 frontend will deal with it
		return userService.findUsers();
	}

	@PostMapping("")
	public User addUser(@RequestBody User user) {
		// problem1: what if body is null or empty?
		// problem2: what if user has non-existant properties?
		logger.info("----------------------> Entering /add");
		
		return userService.saveUser(user);
	}

	@DeleteMapping("/{id}")
	public void deleteUser(@PathVariable long id) {
		//problem1: what if id is not long?  
		//console: [org.springframework.web.method.annotation.MethodArgumentTypeMismatchException:
		//Postman: "status": 400, "error": "Bad Request",
		userService.deleteById(id); 
	}

	@PutMapping("")
	public User updateUser(@RequestBody User user) {
		// problem1: what if body is null or empty?
		// problem2: what if user has non-existant properties?
		return userService.updateUser(user);
	}

	@GetMapping("/{id}")
	public User findUser(@PathVariable long id) {
		//if(id>0)throw new UserNotFoundException();
		//problem1: what if id is not long?
		logger.info("----------------------> UserController /find/{id}: findUser, id:" + id);
		User user = userService.findById(id);
		logger.info("----------------------> UserController /find/{id}: findUser, user:" + user);
		return user;
	}
	
	// spring by default sends some information about the error in response body 
	// when we take control of handling the error, the response body will be empty 
	// but the client(Postman) will see the response status we declared in the handler 
	
	@ExceptionHandler(UserNotFoundException.class)
	@ResponseStatus(value= HttpStatus.BAD_REQUEST) // 400
	public void handleUserNotFoundException() {
		
	}
	// catches all exceptions but not those handled by us
	@ExceptionHandler(Exception.class)
	@ResponseStatus(value= HttpStatus.INTERNAL_SERVER_ERROR) //500
	public void handleOtherExceptions() {
		
	}

	
}