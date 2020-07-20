package com.iavtar.orchestrationservice.service;

import org.springframework.http.ResponseEntity;

public interface OrderService {

	ResponseEntity<?> createOrder(Long userId);

}
