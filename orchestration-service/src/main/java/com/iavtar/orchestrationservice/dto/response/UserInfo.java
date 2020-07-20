package com.iavtar.orchestrationservice.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfo {

	private Long userId;
	private String userName;
	private String firstName;
	private String lastName;
	private String email;
	private Address address;
	private Long cartId;
	
}
