package com.iavtar.cartservice.request.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RemoveProductFromCartRequest {

	private Long cartId;
	private Long productId;
	
}
