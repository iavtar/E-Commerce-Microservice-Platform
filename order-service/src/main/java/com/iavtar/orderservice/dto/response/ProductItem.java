package com.iavtar.orderservice.dto.response;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductItem {

	private Long id;
	private Long productId;
	private String productName;
	private Long productQuantity;
	private BigDecimal totalPrice;
	private BigDecimal pricePerUnit;
	private String productImageUrl;
	
}
