package com.iavtar.orchestrationservice.dto.response;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItem {

	private Long id;
	private Long productId;
	private String productName;
	private Long productQuantity;
	private BigDecimal totalPrice;
	private BigDecimal pricePerUnit;
	private String productImageUrl;

}
