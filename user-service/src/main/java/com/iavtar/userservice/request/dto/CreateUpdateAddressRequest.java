package com.iavtar.userservice.request.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUpdateAddressRequest {

	private Long userId;
	private String addressLine;
	private String city;
	private String pin;
	private String state;
	private String country;

}
