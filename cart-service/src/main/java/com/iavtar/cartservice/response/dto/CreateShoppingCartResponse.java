package com.iavtar.cartservice.response.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateShoppingCartResponse {

	private String code;
	private String message;
	private Long cartId;
	
}
