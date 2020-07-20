package com.iavtar.orchestrationservice.dto.request;

import java.math.BigDecimal;
import java.util.List;

import com.iavtar.orchestrationservice.dto.response.CartItem;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateOrderRequest {
	
	private Long cartId;
	private BigDecimal totalOrderPrice;
	private List<CartItem> orderItems;

}
