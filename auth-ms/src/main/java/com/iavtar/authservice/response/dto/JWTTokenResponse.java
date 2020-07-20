package com.iavtar.authservice.response.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JWTTokenResponse {

	private boolean success;
	private String token;
	
}
