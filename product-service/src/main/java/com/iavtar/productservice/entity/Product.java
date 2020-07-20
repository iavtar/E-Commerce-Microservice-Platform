package com.iavtar.productservice.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	@Column(columnDefinition = "Text")
	private String description;
	private String brandName;
	private BigDecimal pricePerUnit;
	private BigDecimal productWholeSalePrice;
	private Long noOfStocks;
	private String productImageUrl;

}
