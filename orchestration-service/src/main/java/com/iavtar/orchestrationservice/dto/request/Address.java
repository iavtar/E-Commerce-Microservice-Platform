package com.iavtar.orchestrationservice.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Address {

	private String addressLine;
	private String city;
	private String pin;
	private String state;
	private String country;
}
