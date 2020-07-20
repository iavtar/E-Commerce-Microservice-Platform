package com.iavtar.orchestrationservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iavtar.orchestrationservice.service.ProductService;

@RestController
@RequestMapping("/api/product")
public class ProductController {
	
	@Autowired
	private ProductService productService;

	@GetMapping("/all-products")
	public ResponseEntity<?> getAllProducts(){
		ResponseEntity<?> response = productService.getAllProducts();
		return response;
	}
	
	@PostMapping("/update-product-stock")
	public ResponseEntity<?> updateProductStock(){
		return null;
	}
	
	
}
