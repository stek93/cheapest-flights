package com.stek.flights.cheapestflights.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stek.flights.cheapestflights.model.User;
import com.stek.flights.cheapestflights.service.UserService;

@RestController
@RequestMapping(path = "/users")
public class UserController {

	private final UserService userService;

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping(path = "/registration")
	public ResponseEntity<Void> registration(@RequestBody User user) {
		userService.saveUser(user);
		return ResponseEntity.ok().build();
	}

}
