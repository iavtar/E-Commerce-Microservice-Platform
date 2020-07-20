package com.iavtar.orderservice.service;

import org.springframework.http.ResponseEntity;

import com.iavtar.orderservice.dto.request.CancelOrderRequest;
import com.iavtar.orderservice.dto.request.CreateOrderRequest;
import com.iavtar.orderservice.dto.request.GetAllOrderRequest;

public interface OrderService {

	ResponseEntity<?> createOrder(CreateOrderRequest request);

	ResponseEntity<?> cancelOrder(CancelOrderRequest request);

	ResponseEntity<?> getAllOrders(GetAllOrderRequest request);

}
