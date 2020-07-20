package com.iavtar.orchestrationservice.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {

	private String userId;
	private String userName;
	private String firstName;
	private String lastName;
	private String email;
	private Address address;
	private Long cartId;

}
