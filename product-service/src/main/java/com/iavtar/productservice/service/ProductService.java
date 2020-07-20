package com.iavtar.productservice.service;

import java.math.BigDecimal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface ProductService {

	ResponseEntity<?> addProduct(MultipartFile imageFile, String name, String description, String brandName,
			BigDecimal pricePerUnit, BigDecimal productWholeSalePrice, Long noOfStocks);

	ResponseEntity<?> getAllProducts();

}
