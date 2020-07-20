package com.iavtar.authservice.service;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;

import com.iavtar.authservice.request.dto.AddRoleRequest;

public interface AdminService {

	ResponseEntity<?> addRole(@Valid AddRoleRequest request);

}
