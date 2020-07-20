package com.iavtar.orchestrationservice.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddProductToCartRequest {

	private Long cartId;
	private ProductItem productItem; 
}
