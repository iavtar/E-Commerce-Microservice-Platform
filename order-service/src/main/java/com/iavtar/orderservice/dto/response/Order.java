package com.iavtar.orderservice.dto.response;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Order {

	private Long orderId;
	private BigDecimal totalOrderPrice;
	private String orderStatus;
	List<ProductItem> orderItems;
	private Date createdAt;
	private Date updatedAt;
	
}
