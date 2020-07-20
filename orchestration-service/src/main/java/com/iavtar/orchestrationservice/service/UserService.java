package com.iavtar.orchestrationservice.service;

import org.springframework.http.ResponseEntity;

public interface UserService {

	ResponseEntity<?> getUserInfo(long userId);

}
