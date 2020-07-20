package com.iavtar.orchestrationservice.dto.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShoppingCartResponse {

	private List<CartItem> cartItems;
	
}
