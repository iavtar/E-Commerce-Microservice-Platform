package com.iavtar.cartservice.request.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddProductToCartRequest {

	private Long cartId;
	private ProductItem productItem; 
}
