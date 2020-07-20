package com.iavtar.orchestrationservice.service;

import org.springframework.http.ResponseEntity;

import com.iavtar.orchestrationservice.dto.request.SignInRequest;
import com.iavtar.orchestrationservice.dto.request.SignUpRequest;

public interface AuthService {

	ResponseEntity<?> signUp(SignUpRequest request);

	ResponseEntity<?> signIn(SignInRequest request);

}
