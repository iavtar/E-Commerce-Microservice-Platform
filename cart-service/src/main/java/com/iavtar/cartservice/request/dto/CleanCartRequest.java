package com.iavtar.cartservice.request.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CleanCartRequest {

	private Long cartId;
	private List<Long> productIds;
	
}
