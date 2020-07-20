package com.iavtar.orchestrationservice.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iavtar.orchestrationservice.service.OrderService;

@RestController
@RequestMapping("/api/order")
public class OrderController {

	@Autowired
	private OrderService orderService;
	
	@GetMapping("/all-orders")
	public ResponseEntity<?> userOrders(){
		return null;
	}	
	
	@PostMapping("/create-order")
	public ResponseEntity<?> createOrder(Principal principal){
		
		Long userId = Long.parseLong(principal.getName());
		
		ResponseEntity<?> response = orderService.createOrder(userId);
		
		return response;
		
	}
	
}
