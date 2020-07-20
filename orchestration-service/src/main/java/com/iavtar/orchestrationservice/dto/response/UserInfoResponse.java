package com.iavtar.orchestrationservice.dto.response;

import java.math.BigDecimal;
import java.util.List;

import com.iavtar.orchestrationservice.model.Address;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfoResponse {

	private Long userId;
	private String userName;
	private String firstName;
	private String lastName;
	private String email;
	private Address address;
	private Long cartId;
	private BigDecimal cartTotal;	
	private List<CartItem> cartItems;
	private List<Order> orders;
}
