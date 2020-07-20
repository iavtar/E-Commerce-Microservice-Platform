package com.iavtar.authservice.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvalidLoginResponse {

	private String username;
	private String password;

	public InvalidLoginResponse() {
		this.username = "Invalid Username";
		this.password = "Invalid Password";
	}

}
