package com.iavtar.orchestrationservice.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RemoveProductFromCartRequest {

	private Long cartId;
	private String productId;
	
}
