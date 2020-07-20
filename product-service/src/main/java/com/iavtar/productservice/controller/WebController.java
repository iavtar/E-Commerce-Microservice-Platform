package com.iavtar.productservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iavtar.productservice.service.ProductService;

@RestController
@RequestMapping("/api/web/prodcut")
public class WebController {

	@Autowired
	private ProductService productService;

	@GetMapping("/all-products")
	public ResponseEntity<?> getAllProducts() {
		ResponseEntity<?> response = productService.getAllProducts();
		return response;
	}

}
