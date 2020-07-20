package com.iavtar.orchestrationservice.dto.response;

import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Order {

	private String orderId;
	private List<OrderItem> orderItems;
	private String totalOrderPrice;
	private String orderStatus;
	private Date createdAt;
}
