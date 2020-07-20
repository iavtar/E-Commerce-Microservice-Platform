package com.iavtar.orchestrationservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iavtar.orchestrationservice.dto.request.SignInRequest;
import com.iavtar.orchestrationservice.dto.request.SignUpRequest;
import com.iavtar.orchestrationservice.service.AuthService;

@RestController
@RequestMapping("/api/auth")
//@PreAuthorize("hasRole('USER')")
public class AuthController {

	@Autowired
	private AuthService authService;
	
	@PostMapping("/signup")
	public ResponseEntity<?> signUp(@RequestBody SignUpRequest request){
		ResponseEntity<?> response = authService.signUp(request);
		return response;
	}
	
	@PostMapping("/signin")
	public ResponseEntity<?> signIn(@RequestBody SignInRequest request){
		ResponseEntity<?> response = authService.signIn(request);
		return response;
	}
	
}
