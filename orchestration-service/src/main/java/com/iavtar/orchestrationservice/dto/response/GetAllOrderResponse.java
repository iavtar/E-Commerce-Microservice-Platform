package com.iavtar.orchestrationservice.dto.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetAllOrderResponse {
	
	
	private List<Order> orders;

}
