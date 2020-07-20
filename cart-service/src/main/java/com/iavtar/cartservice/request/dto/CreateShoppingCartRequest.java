package com.iavtar.cartservice.request.dto;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateShoppingCartRequest {

	@NotBlank(message = "userId is required to create user shopping cart")
	private Long userId;
	
}
