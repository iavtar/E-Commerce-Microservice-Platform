package com.iavtar.orchestrationservice.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.iavtar.orchestrationservice.dto.request.CleanCartRequest;
import com.iavtar.orchestrationservice.dto.request.CreateOrderRequest;
import com.iavtar.orchestrationservice.dto.request.GetCartItemRequest;
import com.iavtar.orchestrationservice.dto.request.UserRequest;
import com.iavtar.orchestrationservice.dto.response.CartItem;
import com.iavtar.orchestrationservice.dto.response.ServiceResponse;
import com.iavtar.orchestrationservice.dto.response.ShoppingCartResponse;
import com.iavtar.orchestrationservice.dto.response.UserInfo;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

	@Autowired
	private Environment environment;

	@Autowired
	private RestTemplate restTemplate;

	@Override
	public ResponseEntity<?> createOrder(Long userId) {

		ResponseEntity<?> responseEntity = null;
		
		ServiceResponse response = null;
		
		CreateOrderRequest createOrderRequest = new CreateOrderRequest();
		BigDecimal totalCartSum = new BigDecimal(0);
		
		try {
			UserInfo userInfoResponse = getUserInfoFromUserMS(userId);
			createOrderRequest.setCartId(userInfoResponse.getCartId());
			List<CartItem> cartItems = getUserCartItems(userInfoResponse.getCartId());
			createOrderRequest.setOrderItems(cartItems);
			
			List<Long> productIds = new ArrayList<>();
			
			for(int i = 0; i < cartItems.size(); i++) {
				totalCartSum = totalCartSum.add(cartItems.get(i).getTotalPrice());
				productIds.add(cartItems.get(i).getProductId());
			}
			
			createOrderRequest.setTotalOrderPrice(totalCartSum);
			
			response = createOrderInOrderMS(createOrderRequest);			
			
			if(response.getCode().equals("200")) {
				response = cleanCart(productIds, userInfoResponse.getCartId());
			}
			
			responseEntity = new ResponseEntity<>(response, HttpStatus.OK);

		} catch (Exception e) {
			response.setCode("500");
			response.setMessage("Internal Server Error");
		}

		return responseEntity;
	}

	private ServiceResponse cleanCart(List<Long> productIds, Long cartId) {
		
		String userUrl = environment.getProperty("api.cleanCartItems.url");
		ServiceResponse res = new ServiceResponse();

		HttpHeaders headers = new HttpHeaders();
		headers.setBasicAuth(environment.getProperty("shopping-cart-service.username"),
				environment.getProperty("shopping-cart-service.password"));
		CleanCartRequest cleanCartRequest = new CleanCartRequest();
		cleanCartRequest.setCartId(cartId);
		cleanCartRequest.setProductIds(productIds);
		HttpEntity<CleanCartRequest> request = new HttpEntity<CleanCartRequest>(cleanCartRequest, headers);
		ResponseEntity<ServiceResponse> cleanCartResponse = restTemplate.exchange(userUrl, HttpMethod.POST, request, ServiceResponse.class);
		ServiceResponse serviceResponse = cleanCartResponse.getBody();
		if(serviceResponse.getCode().equals("200")) {			
			res.setCode("200");
			res.setMessage("Order Placed Successfully");
		}		
		return res;
	}

	private ServiceResponse createOrderInOrderMS(CreateOrderRequest createOrderRequest) {
		
		String userUrl = environment.getProperty("api.orderservice.createOrder.url");
		ServiceResponse res = new ServiceResponse();
		HttpHeaders headers = new HttpHeaders();
		headers.setBasicAuth(environment.getProperty("order-service.username"),
				environment.getProperty("order-service.password"));
		HttpEntity<CreateOrderRequest> request = new HttpEntity<CreateOrderRequest>(createOrderRequest, headers);
		try {
			ResponseEntity<ServiceResponse> cleanCartResponse = restTemplate.exchange(userUrl, HttpMethod.POST, request, ServiceResponse.class);
			ServiceResponse serviceResponse = cleanCartResponse.getBody();
			if(serviceResponse.getCode().equals("200")) {			
				res.setCode("200");
				res.setMessage("Order Placed");
			}		
		} catch (Exception e) {
			log.error(e.toString());
		}	
		return res;
	}

	@HystrixCommand(fallbackMethod = "getUserInfoFromUserMSFallback")
	private UserInfo getUserInfoFromUserMS(long userId) {

		String userUrl = environment.getProperty("api.userservice.getuserinfo.url");

		HttpHeaders headers = new HttpHeaders();
		headers.setBasicAuth(environment.getProperty("user-service.username"),
				environment.getProperty("user-service.password"));
		UserRequest userRequest = new UserRequest();
		userRequest.setUserId(userId);
		HttpEntity<UserRequest> request = new HttpEntity<UserRequest>(userRequest, headers);
		ResponseEntity<UserInfo> userInfoResponse = restTemplate.exchange(userUrl, HttpMethod.POST, request,
				UserInfo.class);
		UserInfo userInfo = userInfoResponse.getBody();
		if (!(userInfo.getUserId() == 0)) {
			return userInfo;
		}
		return null;
	}

	@HystrixCommand(fallbackMethod = "getUserCartInfoFromCartMSFallback")
	private List<CartItem> getUserCartItems(Long cartId) {
		
		String cartUrl = environment.getProperty("api.getAllCartItems.url");
		HttpHeaders headers = new HttpHeaders();
		headers.setBasicAuth(environment.getProperty("shopping-cart-service.username"),
				environment.getProperty("shopping-cart-service.password"));
		
		GetCartItemRequest cartItemRequest = new GetCartItemRequest();
		cartItemRequest.setCartId(cartId);
		HttpEntity<GetCartItemRequest> request = new HttpEntity<GetCartItemRequest>(cartItemRequest, headers);
		ResponseEntity<ShoppingCartResponse> shoppingCartResponse = restTemplate.exchange(cartUrl, HttpMethod.POST, request, ShoppingCartResponse.class);
		ShoppingCartResponse res = shoppingCartResponse.getBody();
		if(!(res.getCartItems().size() == 0)) {
			return res.getCartItems();
		}
		return null;
	}
	
	ResponseEntity<?> getUserInfoFromUserMSFallback(long userId) {
		ServiceResponse res = new ServiceResponse();
		res.setCode("404");
		res.setMessage("User Service Currently Unavailable");
		return new ResponseEntity<ServiceResponse>(res, HttpStatus.NOT_FOUND);
	}
	
	ResponseEntity<?> getUserCartInfoFromCartMSFallback(long cartId){
		ServiceResponse res = new ServiceResponse();
		res.setCode("404");
		res.setMessage("Cart Service Currently Unavailable");
		return new ResponseEntity<ServiceResponse>(res, HttpStatus.NOT_FOUND);
	}

}
