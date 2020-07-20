package com.iavtar.productservice.controller;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.iavtar.productservice.service.ProductService;

@RestController
@RequestMapping("/api/admin/product")
public class AdminController {

	@Autowired
	private ProductService productService;
	
	@PostMapping("/add-product")
	public ResponseEntity<?> addProduct(
			@RequestParam("imageFile") MultipartFile imageFile,
			@RequestParam("name") String name,
			@RequestParam("description") String description,
			@RequestParam("brandName") String brandName,
			@RequestParam("pricePerUnit") BigDecimal pricePerUnit,
			@RequestParam("productWholeSalePrice") BigDecimal productWholeSalePrice,
			@RequestParam("noOfStocks") Long noOfStocks
			){
		ResponseEntity<?> response = productService.addProduct(imageFile, name, description, brandName, pricePerUnit, productWholeSalePrice, noOfStocks);
		
		return response;
	}	
	
}
