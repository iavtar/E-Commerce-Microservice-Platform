package com.iavtar.userservice.response.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Address {

	private Long addressId;
	private String addressLine;
	private String city;
	private String pin;
	private String state;
	private String country;
}
