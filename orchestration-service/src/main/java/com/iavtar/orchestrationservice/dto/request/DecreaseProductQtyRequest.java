package com.iavtar.orchestrationservice.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DecreaseProductQtyRequest {
	
	private Long cartId;
	private Long productId;

}
