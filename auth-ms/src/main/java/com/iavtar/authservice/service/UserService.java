package com.iavtar.authservice.service;

import org.springframework.http.ResponseEntity;

import com.iavtar.authservice.request.dto.SignUpRequest;

public interface UserService {

	ResponseEntity<?> signUp(SignUpRequest request);
	
}
