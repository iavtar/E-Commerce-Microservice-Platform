package com.iavtar.authservice.request.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpRequest {

	private String username;
	@Email(message = "email is required")
	private String email;
	@NotBlank(message = "first name is required")
	private String firstName;
	@NotBlank(message = "last name is required")
	private String lastName;
	@NotBlank(message = "password is required")
	private String password;
	@NotBlank(message = "re-enter Password")
	private String confirmPassword;
	
}
