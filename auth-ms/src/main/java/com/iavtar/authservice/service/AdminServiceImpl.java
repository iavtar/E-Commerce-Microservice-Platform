package com.iavtar.authservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.iavtar.authservice.entity.Role;
import com.iavtar.authservice.repository.RoleRepository;
import com.iavtar.authservice.request.dto.AddRoleRequest;
import com.iavtar.authservice.response.dto.ServiceResponse;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AdminServiceImpl implements AdminService {

	@Autowired
	private RoleRepository roleRepository;

	@Override
	public ResponseEntity<?> addRole(AddRoleRequest request) {

		ResponseEntity<?> response;
		ServiceResponse serviceResponse = new ServiceResponse();

		try {
			Role savedRole = roleRepository.findRoleByName(request.getRoleName());
			if (!(savedRole == null)) {
				serviceResponse.setCode("409");
				serviceResponse.setMessage("A Role with {" + request.getRoleName() + "} already existed");
				response = new ResponseEntity<ServiceResponse>(serviceResponse, HttpStatus.CONFLICT);
				return response;
			} else {
				Role role = new Role();
				role.setName(request.getRoleName());
				roleRepository.save(role);
				serviceResponse.setCode("201");
				serviceResponse.setMessage("Role added successfully");
				response = new ResponseEntity<ServiceResponse>(serviceResponse, HttpStatus.CREATED);
				return response;
			}

		} catch (Exception e) {
			log.error("There is an error while adding role : " + e.getMessage());
			serviceResponse.setCode("501");
			serviceResponse.setMessage("There is an error while adding role");
			response = new ResponseEntity<ServiceResponse>(serviceResponse, HttpStatus.CREATED);
			return response;
		}
	}

}
