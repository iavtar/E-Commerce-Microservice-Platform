package com.iavtar.authservice.response.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpResponse {

	private Long userId;
	private String code;
	private String message;
	
}
