package com.iavtar.orchestrationservice.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JWTTokenResponse {

	private boolean success;
	private String token;
	
}
