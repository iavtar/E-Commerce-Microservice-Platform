package com.iavtar.userservice.response.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfoResponse {

	private Long userId;
	private String userName;
	private String firstName;
	private String lastName;
	private String email;
	private Address address;
	private Long cartId;
	
}
