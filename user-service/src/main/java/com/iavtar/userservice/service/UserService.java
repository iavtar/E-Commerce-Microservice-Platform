package com.iavtar.userservice.service;

import org.springframework.http.ResponseEntity;

import com.iavtar.userservice.request.dto.CreateUpdateAddressRequest;
import com.iavtar.userservice.request.dto.CreateUserRequest;
import com.iavtar.userservice.request.dto.UserInfoRequest;

public interface UserService {

	ResponseEntity<?> createUser(CreateUserRequest request);

	ResponseEntity<?> getUserInfo(UserInfoRequest request);

	ResponseEntity<?> createUpdateAddress(CreateUpdateAddressRequest request);

}
