package com.iavtar.orchestrationservice.dto.response;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Product {

	private Long id;
	private String name;
	private String description;
	private String brandName;
	private BigDecimal pricePerUnit;
	private Long noOfStocks;
	private String productImageUrl;

}
