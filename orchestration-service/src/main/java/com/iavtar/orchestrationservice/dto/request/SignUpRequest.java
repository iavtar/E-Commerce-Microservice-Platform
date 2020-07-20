package com.iavtar.orchestrationservice.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpRequest {

	private String username;
	private String lastName;
	private String firstName;
	private String email;
	private String password;
	private String confirmPassword;
	
}
