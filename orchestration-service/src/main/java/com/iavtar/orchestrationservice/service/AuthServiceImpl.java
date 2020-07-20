package com.iavtar.orchestrationservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iavtar.orchestrationservice.dto.request.CreateShoppingCartRequest;
import com.iavtar.orchestrationservice.dto.request.CreateUserRequest;
import com.iavtar.orchestrationservice.dto.request.SignInRequest;
import com.iavtar.orchestrationservice.dto.request.SignUpRequest;
import com.iavtar.orchestrationservice.dto.response.CreateShoppingCartResponse;
import com.iavtar.orchestrationservice.dto.response.JWTTokenResponse;
import com.iavtar.orchestrationservice.dto.response.ServiceResponse;
import com.iavtar.orchestrationservice.dto.response.SignInResponse;
import com.iavtar.orchestrationservice.dto.response.SignUpResponse;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

	@Autowired
	private Environment environment;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ObjectMapper objectMapper;

//	@HystrixCommand(fallbackMethod = "getSignUpFallback")
//	@Override
//	public ResponseEntity<?> signUp(SignUpRequest request) {
//
//		ServiceResponse serviceResponse = new ServiceResponse();
//		SignUpResponse signUpResponse = new SignUpResponse();
//		try {
//			String url = environment.getProperty("api.signup.url");
//			String createShoppingCartUrl = environment.getProperty("api.createShoppingcart.url");
//			log.info("Before Calling Auth Service");
//			String res = restTemplate.postForObject(url, request, String.class);
//			log.info("After Calling Auth Service");
//			signUpResponse = objectMapper.readValue(res, SignUpResponse.class);
//			String responseCode = signUpResponse.getCode();
//			Long userId = signUpResponse.getUserId();
//			if (responseCode.equals("200")) {
//				HttpHeaders headers = new HttpHeaders();
//				headers.setBasicAuth(environment.getProperty("shopping-cart-service.username"), environment.getProperty("shopping-cart-service.password"));
//				CreateShoppingCartRequest csCart = new CreateShoppingCartRequest();
//				csCart.setUserId(userId);
//				HttpEntity<CreateShoppingCartRequest> createCartTequest = new HttpEntity<>(csCart, headers);
//				try {
//					String cartRes = restTemplate.postForObject(createShoppingCartUrl, createCartTequest, String.class);
//					serviceResponse = objectMapper.readValue(cartRes, ServiceResponse.class);
//				} catch (Exception e) {
//					log.error("Cart Service Not responding");
//				}
//				serviceResponse.setCode("200");
//				serviceResponse.setMessage("User Registered Successfully");
//				return new ResponseEntity<ServiceResponse>(serviceResponse, HttpStatus.OK);
//			} else if (responseCode.equals("400")) {
//				serviceResponse.setCode("400");
//				serviceResponse.setMessage("User Already Registered with username " + request.getUsername());
//				return new ResponseEntity<ServiceResponse>(serviceResponse, HttpStatus.CONFLICT);
//			} else if (responseCode.equals("401")) {
//				serviceResponse.setCode("401");
//				serviceResponse.setMessage("We are not accepting registration at the moment. Please try later!");
//				return new ResponseEntity<ServiceResponse>(serviceResponse, HttpStatus.UNAUTHORIZED);
//			} else if (responseCode.equals("501")) {
//				serviceResponse.setCode("501");
//				serviceResponse.setMessage("Internal Server Error");
//				return new ResponseEntity<ServiceResponse>(serviceResponse, HttpStatus.INTERNAL_SERVER_ERROR);
//			}
//		} catch (Exception e) {
//			log.error(e.toString());
//			serviceResponse.setCode("501");
//			serviceResponse.setMessage("Internal Server Error");
//			return new ResponseEntity<ServiceResponse>(serviceResponse, HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//
//		return null;
//
//	}

//	@HystrixCommand(fallbackMethod = "getSignUpFallback")
//	@Override
//	public ResponseEntity<?> signUp(SignUpRequest request) {
//
//		ServiceResponse serviceResponse = new ServiceResponse();
//		SignUpResponse signUpResponse = new SignUpResponse();
//		ResponseEntity<?> responseEntity = null;
//		
//			String url = environment.getProperty("api.signup.url");
//			String createShoppingCartUrl = environment.getProperty("api.createShoppingcart.url");
//			String userServiceUrl = environment.getProperty("api.userservice.createUser.url");
//			log.info("Before Calling Auth Service");
//			String res = restTemplate.postForObject(url, request, String.class);
//			log.info("After Calling Auth Service");
//			try {
//				signUpResponse = objectMapper.readValue(res, SignUpResponse.class);
//			} catch (JsonProcessingException e) {
//				e.printStackTrace();
//			}
//			String responseCode = signUpResponse.getCode();
//			Long userId = signUpResponse.getUserId();
//			if (responseCode.equals("200")) {
//				HttpHeaders headers = new HttpHeaders();
//				headers.setBasicAuth(environment.getProperty("shopping-cart-service.username"), environment.getProperty("shopping-cart-service.password"));
//				CreateShoppingCartRequest csCart = new CreateShoppingCartRequest();
//				csCart.setUserId(userId);
//				HttpEntity<CreateShoppingCartRequest> createCartRequest = new HttpEntity<>(csCart, headers);
//				
//					String cartRes = restTemplate.postForObject(createShoppingCartUrl, createCartRequest, String.class);
//					try {
//						serviceResponse = objectMapper.readValue(cartRes, ServiceResponse.class);
//					} catch (JsonProcessingException e) {
//						e.printStackTrace();
//					}				
//				serviceResponse.setCode("200");
//				serviceResponse.setMessage("User Registered Successfully");
//				return new ResponseEntity<ServiceResponse>(serviceResponse, HttpStatus.OK);
//			} else if (responseCode.equals("400")) {
//				serviceResponse.setCode("400");
//				serviceResponse.setMessage("User Already Registered with username " + request.getUsername());
//				responseEntity = new ResponseEntity<ServiceResponse>(serviceResponse, HttpStatus.CONFLICT);
//			} else if (responseCode.equals("401")) {
//				serviceResponse.setCode("401");
//				serviceResponse.setMessage("We are not accepting registration at the moment. Please try later!");
//				responseEntity = new ResponseEntity<ServiceResponse>(serviceResponse, HttpStatus.UNAUTHORIZED);
//			} else if (responseCode.equals("501")) {
//				serviceResponse.setCode("501");
//				serviceResponse.setMessage("Internal Server Error");
//				responseEntity = new ResponseEntity<ServiceResponse>(serviceResponse, HttpStatus.INTERNAL_SERVER_ERROR);
//			}
//		
//
//		return responseEntity;
//
//	}

	@HystrixCommand(fallbackMethod = "getSignUpFallback")
	@Override
	public ResponseEntity<?> signUp(SignUpRequest request) {

		ServiceResponse serviceResponse = new ServiceResponse();
		SignUpResponse signUpResponse = new SignUpResponse();
		ResponseEntity<?> responseEntity = null;

		String url = environment.getProperty("api.signup.url");
		String createShoppingCartUrl = environment.getProperty("api.createShoppingcart.url");
		String userServiceUrl = environment.getProperty("api.userservice.createUser.url");
		log.info("Before Calling Auth Service");
		String res = restTemplate.postForObject(url, request, String.class);
		log.info("After Calling Auth Service");
		try {
			signUpResponse = objectMapper.readValue(res, SignUpResponse.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		String responseCode = signUpResponse.getCode();
		Long userId = signUpResponse.getUserId();
		if (responseCode.equals("200")) {
			HttpHeaders headers = new HttpHeaders();
			headers.setBasicAuth(environment.getProperty("shopping-cart-service.username"),
					environment.getProperty("shopping-cart-service.password"));
			CreateShoppingCartRequest csCart = new CreateShoppingCartRequest();
			csCart.setUserId(userId);
			HttpEntity<CreateShoppingCartRequest> createCartRequest = new HttpEntity<>(csCart, headers);

			String cartRes = restTemplate.postForObject(createShoppingCartUrl, createCartRequest, String.class);
			try {
				CreateShoppingCartResponse cartResponse = new CreateShoppingCartResponse();
				cartResponse = objectMapper.readValue(cartRes, CreateShoppingCartResponse.class);
				if (!(cartResponse.getCartId() == null)) {
					boolean createUserInfo = registerUserInUserMS(request, cartResponse.getCartId(), userId);
					if (createUserInfo) {
						log.info("User registered with user microservice");
					} else {
						log.error("User is not regiastered with user microservice");
					}

				}
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
			serviceResponse.setCode("200");
			serviceResponse.setMessage("User Registered Successfully");
			return new ResponseEntity<ServiceResponse>(serviceResponse, HttpStatus.OK);
		} else if (responseCode.equals("400")) {
			serviceResponse.setCode("400");
			serviceResponse.setMessage("User Already Registered with username " + request.getUsername());
			responseEntity = new ResponseEntity<ServiceResponse>(serviceResponse, HttpStatus.CONFLICT);
		} else if (responseCode.equals("401")) {
			serviceResponse.setCode("401");
			serviceResponse.setMessage("We are not accepting registration at the moment. Please try later!");
			responseEntity = new ResponseEntity<ServiceResponse>(serviceResponse, HttpStatus.UNAUTHORIZED);
		} else if (responseCode.equals("501")) {
			serviceResponse.setCode("501");
			serviceResponse.setMessage("Internal Server Error");
			responseEntity = new ResponseEntity<ServiceResponse>(serviceResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return responseEntity;

	}

	private boolean registerUserInUserMS(SignUpRequest request, Long cartId, Long userId) {

		boolean userRegInUserMS = false;

		String setUserInfoUrl = environment.getProperty("api.userservice.createUser.url");
		HttpHeaders headers = new HttpHeaders();
		headers.setBasicAuth(environment.getProperty("user-service.username"),
				environment.getProperty("user-service.password"));
		CreateUserRequest createUserRequest = new CreateUserRequest();

		createUserRequest.setUserId(userId);
		createUserRequest.setFirstName(request.getFirstName());
		createUserRequest.setLastName(request.getLastName());
		createUserRequest.setUserName(request.getUsername());
		createUserRequest.setEmail(request.getEmail());
		createUserRequest.setCartId(cartId);

		HttpEntity<CreateUserRequest> cUserRequest = new HttpEntity<>(createUserRequest, headers);

		String userServiceResponse = restTemplate.postForObject(setUserInfoUrl, cUserRequest, String.class);

		try {
			ServiceResponse response = new ServiceResponse();
			response = objectMapper.readValue(userServiceResponse, ServiceResponse.class);
			if (response.getCode().equals("200")) {
				userRegInUserMS = true;
			}
		} catch (Exception e) {
			log.error(e.toString());
		}

		return userRegInUserMS;
	}

	public ResponseEntity<?> getSignUpFallback(SignUpRequest request) {
		ServiceResponse res = new ServiceResponse();
		res.setCode("404");
		res.setMessage("Authentication Service Currently Unavailable");
		return new ResponseEntity<ServiceResponse>(res, HttpStatus.NOT_FOUND);
	}

//	@HystrixCommand(fallbackMethod = "getSignInFallback")
//	@Override
//	public ResponseEntity<?> signIn(SignInRequest request) {
//
//		String url = environment.getProperty("api.signin.url");
//
//		ServiceResponse serviceResponse = new ServiceResponse();
//
//		try {
//			JWTTokenResponse response = null;
//			String res = restTemplate.postForObject(url, request, String.class);
//			response = objectMapper.readValue(res, JWTTokenResponse.class);
//			if (response.isSuccess() == true) {
//				SignInResponse sresponse = new SignInResponse();
//				sresponse.setToken(response.getToken());
//				return new ResponseEntity<SignInResponse>(sresponse, HttpStatus.OK);
//			}
//		} catch (Exception e) {
//			serviceResponse.setCode("401");
//			serviceResponse.setMessage("Invalid Credentials");
//			return new ResponseEntity<ServiceResponse>(serviceResponse, HttpStatus.UNAUTHORIZED);
//		}
//		return null;
//	}

	@HystrixCommand(fallbackMethod = "getSignInFallback")
	@Override
	public ResponseEntity<?> signIn(SignInRequest request) {
		String url = environment.getProperty("api.signin.url");
		ResponseEntity<?> resEntity;
		ServiceResponse serviceResponse = new ServiceResponse();

		JWTTokenResponse response = null;
		String res = restTemplate.postForObject(url, request, String.class);
		try {
			response = objectMapper.readValue(res, JWTTokenResponse.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		SignInResponse sresponse = new SignInResponse();
		sresponse.setToken(response.getToken());
		resEntity = new ResponseEntity<SignInResponse>(sresponse, HttpStatus.OK);
		return resEntity;
	}

	public ResponseEntity<?> getSignInFallback(SignInRequest request) {
		ServiceResponse res = new ServiceResponse();
		res.setCode("404");
		res.setMessage("Authentication Service Currently Unavailable");
		return new ResponseEntity<ServiceResponse>(res, HttpStatus.NOT_FOUND);
	}

}
