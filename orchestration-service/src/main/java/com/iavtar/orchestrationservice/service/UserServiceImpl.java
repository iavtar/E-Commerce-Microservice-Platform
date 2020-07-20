package com.iavtar.orchestrationservice.service;

import java.math.BigDecimal;
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

import com.iavtar.orchestrationservice.dto.request.GetAllOrderRequest;
import com.iavtar.orchestrationservice.dto.request.GetCartItemRequest;
import com.iavtar.orchestrationservice.dto.request.UserRequest;
import com.iavtar.orchestrationservice.dto.response.CartItem;
import com.iavtar.orchestrationservice.dto.response.GetAllOrderResponse;
import com.iavtar.orchestrationservice.dto.response.Order;
import com.iavtar.orchestrationservice.dto.response.ServiceResponse;
import com.iavtar.orchestrationservice.dto.response.ShoppingCartResponse;
import com.iavtar.orchestrationservice.dto.response.UserInfo;
import com.iavtar.orchestrationservice.dto.response.UserInfoResponse;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

	@Autowired
	private Environment environment;

	@Autowired
	private RestTemplate restTemplate;

	@HystrixProperty(name = "hystrix.command.default.execution.timeout.enabled", value = "30000")
	@Override
	public ResponseEntity<?> getUserInfo(long userId) {
		
		ResponseEntity<?> responseEntity = null;
		
		UserInfoResponse userInfoResult = new UserInfoResponse();
		
		try {
			UserInfo userInfoResponse = getUserInfoFromUserMS(userId);
			userInfoResult.setUserId(userInfoResponse.getUserId());
			userInfoResult.setUserName(userInfoResponse.getUserName());
			userInfoResult.setFirstName(userInfoResponse.getFirstName());
			userInfoResult.setLastName(userInfoResponse.getLastName());
			userInfoResult.setEmail(userInfoResponse.getEmail());
			userInfoResult.setCartId(userInfoResponse.getCartId());			
			
			List<CartItem> cartItems = getUserCartItems(userInfoResponse.getCartId());
			
			BigDecimal cartTotal = new BigDecimal(0);
			
			if(!(cartItems == null)) {
				for(int i = 0; i < cartItems.size(); i++) {
					cartTotal = cartTotal.add(cartItems.get(i).getTotalPrice());
				}
				userInfoResult.setCartTotal(cartTotal);
				userInfoResult.setCartItems(cartItems);
			}						
			
			List<Order> orders = getUserOrders(userInfoResponse.getCartId());
			
			if(!(orders == null)) {
				userInfoResult.setOrders(orders);
			}			
			
			responseEntity = new ResponseEntity<>(userInfoResult, HttpStatus.OK);
			
		} catch (Exception e) {
			log.error(e.toString());
		}
		
		
	
		return responseEntity;
	}

	@HystrixProperty(name = "hystrix.command.default.execution.timeout.enabled", value = "30000")
	private List<Order> getUserOrders(Long cartId) {
		String url = environment.getProperty("api.orderservice.getAllOrders.url");
		HttpHeaders headers = new HttpHeaders();
		headers.setBasicAuth(environment.getProperty("order-service.username"),
				environment.getProperty("order-service.password"));
		GetAllOrderRequest getAllOrderRequest = new GetAllOrderRequest();
		getAllOrderRequest.setCartId(cartId);
		HttpEntity<GetAllOrderRequest> request = new HttpEntity<GetAllOrderRequest>(getAllOrderRequest, headers);
		try {
			ResponseEntity<GetAllOrderResponse> shoppingCartResponse = restTemplate.exchange(url, HttpMethod.POST, request, GetAllOrderResponse.class);
			GetAllOrderResponse orderList = shoppingCartResponse.getBody();
			if(!(orderList.getOrders().size() == 0)) {
				List<Order> list = orderList.getOrders();
				return list;
			}
		} catch (Exception e) {
			log.error(e.toString());
			return null;
		}		
		return null;
	}

	@HystrixProperty(name = "hystrix.command.default.execution.timeout.enabled", value = "30000")
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

	@HystrixProperty(name = "hystrix.command.default.execution.timeout.enabled", value = "30000")
	@HystrixCommand(fallbackMethod = "getUserInfoFromUserMSFallback")
	private UserInfo getUserInfoFromUserMS(long userId) {
		
		String userUrl = environment.getProperty("api.userservice.getuserinfo.url");
		
		HttpHeaders headers = new HttpHeaders();
		headers.setBasicAuth(environment.getProperty("user-service.username"),
				environment.getProperty("user-service.password"));
		UserRequest userRequest = new UserRequest();
		userRequest.setUserId(userId);
		HttpEntity<UserRequest> request = new HttpEntity<UserRequest>(userRequest, headers);
		ResponseEntity<UserInfo> userInfoResponse = restTemplate.exchange(userUrl, HttpMethod.POST, request, UserInfo.class);
		UserInfo userInfo = userInfoResponse.getBody();
		if(!(userInfo.getUserId() == 0)) {
			return userInfo;
		}		
		return null;
	}
	
	ResponseEntity<?> getUserInfoFromUserMSFallback(long userId){
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
