package com.iavtar.orderservice.dto.request;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderProductItem {
	
	private Long productId;
	private String productName;
	private Long productQuantity;
	private BigDecimal totalPrice;
	private BigDecimal pricePerUnit;
	private String productImageUrl;

}
