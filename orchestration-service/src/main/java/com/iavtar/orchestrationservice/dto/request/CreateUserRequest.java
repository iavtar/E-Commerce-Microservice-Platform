package com.iavtar.orchestrationservice.dto.request;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserRequest {

	private Long userId;
	private String userName;
	private String firstName;
	private String lastName;
	private String email;
	private Address address;
	private Long cartId;
	
}
