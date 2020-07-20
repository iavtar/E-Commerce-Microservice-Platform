package com.iavtar.orchestrationservice.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iavtar.orchestrationservice.service.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@PostMapping("/user-info")	
	public ResponseEntity<?> userInfo(Principal user){
		
		ResponseEntity<?> response = userService.getUserInfo(Long.parseLong(user.getName()));
		
		return response;
	}
	
	
	

}
