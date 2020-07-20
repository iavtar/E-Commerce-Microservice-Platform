package com.iavtar.authservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iavtar.authservice.entity.Role;
import com.iavtar.authservice.entity.User;
import com.iavtar.authservice.model.UserPrincipal;
import com.iavtar.authservice.repository.RoleRepository;
import com.iavtar.authservice.repository.UserRepository;
import com.iavtar.authservice.request.dto.SignUpRequest;
import com.iavtar.authservice.response.dto.ServiceResponse;
import com.iavtar.authservice.response.dto.SignUpResponse;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		User user = userRepository.findUserByUsername(username);
		if (!(user == null)) {
			return UserPrincipal.build(user);
		} else {
			throw new UsernameNotFoundException("User not found with username -> : " + username);
		}
	}

	@Transactional
	public User loadUserById(long id) {
		User user = userRepository.findById(id).orElse(null);
		if (!(user == null)) {
			return user;
		} else {
			throw new UsernameNotFoundException("User not found");
		}
	}

	@Override
	@Transactional
	public ResponseEntity<?> signUp(SignUpRequest request) {

		ResponseEntity<?> response;
		ServiceResponse serviceResponse = new ServiceResponse();
		SignUpResponse signUpResponse = new SignUpResponse();

		try {
			if (!(request.getPassword().equals(request.getConfirmPassword()))) {
				serviceResponse.setCode("400");
				serviceResponse.setMessage("Please enter the same password as in the password field");
				response = new ResponseEntity<ServiceResponse>(serviceResponse, HttpStatus.OK);
				return response;
			} else if (!(userRepository.findUserByUsername(request.getUsername()) == null)) {
				serviceResponse.setCode("400");
				serviceResponse.setMessage("User already registered with " + request.getUsername()
						+ " please try with a different username");
				response = new ResponseEntity<ServiceResponse>(serviceResponse, HttpStatus.OK);
				return response;
			} else {
				User user = new User();
				user.setUsername(request.getUsername());
				user.setEmail(request.getEmail());
				user.setFirstName(request.getFirstName());
				user.setLastName(request.getLastName());
				user.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));

				Role userRole = roleRepository.findRoleByName("ROLE_USER");

				if (!(userRole == null)) {
					userRepository.save(user);
					user.addRole(userRole);
					User registeredUser = userRepository.save(user);
					
					signUpResponse.setUserId(registeredUser.getId());
					signUpResponse.setCode("200");
					signUpResponse.setMessage(request.getUsername() + " you are successfully registered");
					response = new ResponseEntity<SignUpResponse>(signUpResponse, HttpStatus.OK);
					
				} else {
					serviceResponse.setCode("401");
					serviceResponse.setMessage(request.getUsername()
							+ " we are not accepting user registration at the moment. Please try again later");
					response = new ResponseEntity<ServiceResponse>(serviceResponse, HttpStatus.OK);
					return response;
				}
				return response;
			}

		} catch (Exception e) {
			log.error("{ There is an Error while SignUp }" + e.toString());
			serviceResponse.setCode("501");
			serviceResponse.setMessage("There is an error occured while signing you up. Please try after some time");
			response = new ResponseEntity<ServiceResponse>(serviceResponse, HttpStatus.INTERNAL_SERVER_ERROR);
			return response;
		}
	}

}
