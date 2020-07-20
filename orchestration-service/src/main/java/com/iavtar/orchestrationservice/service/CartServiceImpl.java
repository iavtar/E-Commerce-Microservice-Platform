package com.iavtar.orchestrationservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iavtar.orchestrationservice.dto.request.AddProductToCartRequest;
import com.iavtar.orchestrationservice.dto.request.CheckCartPredicate;
import com.iavtar.orchestrationservice.dto.request.CreateShoppingCartRequest;
import com.iavtar.orchestrationservice.dto.request.DecreaseProductQtyRequest;
import com.iavtar.orchestrationservice.dto.request.GetCartItemRequest;
import com.iavtar.orchestrationservice.dto.request.RemoveProductFromCartRequest;
import com.iavtar.orchestrationservice.dto.response.CreateShoppingCartResponse;
import com.iavtar.orchestrationservice.dto.response.ServiceResponse;
import com.netflix.ribbon.proxy.annotation.Http;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CartServiceImpl implements CartService {

	@Autowired
	private Environment environment;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	@Override
	public ResponseEntity<?> addProductToCart(Long userId, AddProductToCartRequest request) {

		ServiceResponse response = new ServiceResponse();
		ResponseEntity<?> responseEntity = null;

		try {
			// check if cart is available for the user
			boolean cartAvailabilityPredicate = checkCartAvailability(userId);

			if (cartAvailabilityPredicate) {
				String url = environment.getProperty("api.addProductToCart.url");
				HttpHeaders headers = new HttpHeaders();
				headers.setBasicAuth(environment.getProperty("shopping-cart-service.username"),
						environment.getProperty("shopping-cart-service.password"));
				HttpEntity<AddProductToCartRequest> addToCartRequest = new HttpEntity<>(request, headers);
				String addToCartResponse = restTemplate.postForObject(url, addToCartRequest, String.class);
				response = objectMapper.readValue(addToCartResponse, ServiceResponse.class);
				if (response.getCode().equals("200")) {
					response.setCode("200");
					response.setMessage("Product Added Successfully");
					responseEntity = new ResponseEntity<ServiceResponse>(response, HttpStatus.OK);
				} else {
					response.setCode("500");
					response.setMessage("Unable to add product, contact administrator");
					responseEntity = new ResponseEntity<ServiceResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
				}

			} else {
				Long cartId = createCart(userId);
				if (!(cartId == 0L)) {
					String url = environment.getProperty("api.addProductToCart.url");
					HttpHeaders headers = new HttpHeaders();
					headers.setBasicAuth(environment.getProperty("shopping-cart-service.username"),
							environment.getProperty("shopping-cart-service.password"));
					request.setCartId(cartId);
					HttpEntity<AddProductToCartRequest> addToCartRequest = new HttpEntity<>(request, headers);
					String addToCartResponse = restTemplate.postForObject(url, addToCartRequest, String.class);
					response = objectMapper.readValue(addToCartResponse, ServiceResponse.class);
					if (response.getCode().equals("200")) {
						response.setCode("200");
						response.setMessage("Product Added Successfully");
						responseEntity = new ResponseEntity<ServiceResponse>(response, HttpStatus.OK);
					} else {
						response.setCode("500");
						response.setMessage("Unable to add product, contact administrator");
						responseEntity = new ResponseEntity<ServiceResponse>(response,
								HttpStatus.INTERNAL_SERVER_ERROR);
					}
				}
			}
		} catch (Exception e) {
			response.setCode("500");
			response.setMessage("Unable to add product, contact administrator");
			responseEntity = new ResponseEntity<ServiceResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return responseEntity;
	}

	@Override
	public ResponseEntity<?> removeProductFromCart(Long userId, RemoveProductFromCartRequest request) {
		ServiceResponse response = new ServiceResponse();
		ResponseEntity<?> responseEntity = null;
		try {
			String url = environment.getProperty("api.removeProductFromCart.url");
			HttpHeaders headers = new HttpHeaders();
			headers.setBasicAuth(environment.getProperty("shopping-cart-service.username"),
					environment.getProperty("shopping-cart-service.password"));
			HttpEntity<RemoveProductFromCartRequest> rPFCRequest = new HttpEntity<>(request, headers);
			String rPFCResponse = restTemplate.postForObject(url, rPFCRequest, String.class);
			response = objectMapper.readValue(rPFCResponse, ServiceResponse.class);
			if (response.getCode().equals("200")) {
				response.setCode("200");
				response.setMessage("Product removed from cart");
				responseEntity = new ResponseEntity<ServiceResponse>(response, HttpStatus.OK);
			} else {
				response.setCode("500");
				response.setMessage("There is an error communicating with cart service");
				responseEntity = new ResponseEntity<ServiceResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			log.error(e.toString());
			response.setCode("500");
			response.setMessage("Internal Server Error");
			responseEntity = new ResponseEntity<ServiceResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return responseEntity;
	}

	@Override
	public ResponseEntity<?> decreaseProductQuantityFromCart(Long userId, DecreaseProductQtyRequest request) {
		ServiceResponse response = new ServiceResponse();
		ResponseEntity<?> responseEntity = null;

		try {
			String url = environment.getProperty("api.decreaseProductQtyFromCart.url");
			HttpHeaders headers = new HttpHeaders();
			headers.setBasicAuth(environment.getProperty("shopping-cart-service.username"),
					environment.getProperty("shopping-cart-service.password"));
			HttpEntity<DecreaseProductQtyRequest> dPQRequest = new HttpEntity<>(request, headers);
			String dPQResponse = restTemplate.postForObject(url, dPQRequest, String.class);
			response = objectMapper.readValue(dPQResponse, ServiceResponse.class);
			if (response.getCode().equals("200")) {
				response.setCode("200");
				response.setMessage("Product qty decreased");
				responseEntity = new ResponseEntity<ServiceResponse>(response, HttpStatus.OK);
			} else if(response.getCode().equals("404")){
				response.setCode("200");
				response.setMessage("Product removed from cart");
				responseEntity = new ResponseEntity<ServiceResponse>(response, HttpStatus.OK);
			}			
			else {
				response.setCode("500");
				response.setMessage("There is an error communicating with cart service");
				responseEntity = new ResponseEntity<ServiceResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			log.error(e.toString());
			response.setCode("500");
			response.setMessage("Internal Server Error");
			responseEntity = new ResponseEntity<ServiceResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return responseEntity;
	}
	
	@Override
	public ResponseEntity<?> getAllCartItems(Long userId, GetCartItemRequest request) {
		
		String url = environment.getProperty("http://cart-service/api/shopping_cart/cart_items");
		HttpHeaders headers = new HttpHeaders();
		headers.setBasicAuth(environment.getProperty("order-service.username"),
				environment.getProperty("order-service.password"));
		HttpEntity<GetCartItemRequest> req = new HttpEntity<>(request, headers);
		
		
		return null;
	}

	private boolean checkCartAvailability(Long userId) {

		boolean isCartAvailable = false;

		ServiceResponse response = new ServiceResponse();

		String url = environment.getProperty("api.checkShoppingCart.url");
		HttpHeaders headers = new HttpHeaders();
		headers.setBasicAuth(environment.getProperty("shopping-cart-service.username"),
				environment.getProperty("shopping-cart-service.password"));
		CheckCartPredicate cCartPredicate = new CheckCartPredicate();
		cCartPredicate.setUserId(userId);
		HttpEntity<CheckCartPredicate> request = new HttpEntity<>(cCartPredicate, headers);

		try {
			String cCartPredicateResponse = restTemplate.postForObject(url, request, String.class);
			response = objectMapper.readValue(cCartPredicateResponse, ServiceResponse.class);
			if (response.getCode().equals("200")) {
				isCartAvailable = true;
			}

		} catch (Exception e) {
			log.error(e.toString());
			isCartAvailable = false;
		}

		return isCartAvailable;
	}

	private Long createCart(Long userId) {

		Long cartId = 0L;

		CreateShoppingCartResponse response = new CreateShoppingCartResponse();

		String url = environment.getProperty("api.createShoppingcart.url");
		HttpHeaders headers = new HttpHeaders();
		headers.setBasicAuth(environment.getProperty("shopping-cart-service.username"),
				environment.getProperty("shopping-cart-service.password"));
		CreateShoppingCartRequest csCart = new CreateShoppingCartRequest();
		csCart.setUserId(userId);
		HttpEntity<CreateShoppingCartRequest> request = new HttpEntity<>(csCart, headers);

		try {
			String cartRes = restTemplate.postForObject(url, request, String.class);
			response = objectMapper.readValue(cartRes, CreateShoppingCartResponse.class);
			if (response.getCode().equals("200")) {
				cartId = response.getCartId();
			}
		} catch (Exception e) {
			log.error(e.toString());
			
		}
		return cartId;
	}

}
