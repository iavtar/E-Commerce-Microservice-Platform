package com.iavtar.orderservice.dto.request;

import java.math.BigDecimal;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateOrderRequest {
	
	private Long cartId;
	private BigDecimal totalOrderPrice;
	private List<OrderProductItem> orderItems;

}
