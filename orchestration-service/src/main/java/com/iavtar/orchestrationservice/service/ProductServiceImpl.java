package com.iavtar.orchestrationservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iavtar.orchestrationservice.dto.response.Product;
import com.iavtar.orchestrationservice.dto.response.ServiceResponse;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private Environment environment;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	@HystrixCommand(fallbackMethod = "getAllProductsFallback")
	@Override
	public ResponseEntity<?> getAllProducts() {

		String url = environment.getProperty("api.all-product.url");
		ServiceResponse serviceResponse = new ServiceResponse();
		
			HttpHeaders headers = new HttpHeaders();
			headers.setBasicAuth(environment.getProperty("productService.username"),
					environment.getProperty("productService.password"));
			HttpEntity<String> request = new HttpEntity<String>(headers);
			ResponseEntity<Product[]> res = restTemplate.exchange(url, HttpMethod.GET, request, Product[].class);
			Product[] prod = res.getBody();
			if (!(prod.length == 0)) {
				return new ResponseEntity<Product[]>(prod, HttpStatus.OK);
			} else {
				serviceResponse.setCode("404");
				serviceResponse.setMessage("No Products Found");
				return new ResponseEntity<ServiceResponse>(serviceResponse, HttpStatus.NOT_FOUND);
			}

	}

	public ResponseEntity<?> getAllProductsFallback() {
		ServiceResponse res = new ServiceResponse();
		res.setCode("404");
		res.setMessage("Product Service Currently Unavailable");
		return new ResponseEntity<ServiceResponse>(res, HttpStatus.NOT_FOUND);
	}

}
