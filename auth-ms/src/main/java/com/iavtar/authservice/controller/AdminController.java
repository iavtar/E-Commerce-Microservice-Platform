package com.iavtar.authservice.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iavtar.authservice.request.dto.AddRoleRequest;
import com.iavtar.authservice.service.AdminService;
import com.iavtar.authservice.service.RequestValidationService;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

	@Autowired
	private RequestValidationService requestValidationService;

	@Autowired
	private AdminService adminService;

	@PostMapping("/add_role")
	public ResponseEntity<?> addRole(@Valid @RequestBody AddRoleRequest request, BindingResult result) {

		ResponseEntity<?> requestErrors = requestValidationService.requestValidator(result);

		if (!(requestErrors == null)) {
			return requestErrors;
		} else {
			ResponseEntity<?> response = adminService.addRole(request);
			return response;
		}

	}

}
