package com.iavtar.productservice.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.iavtar.productservice.dto.response.ServiceResponse;
import com.iavtar.productservice.entity.Product;
import com.iavtar.productservice.repository.ProductRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

	private AmazonS3 s3client;

	@Value("${amazonProperties.endpointUrl}")
	private String endpointUrl;
	@Value("${amazonProperties.bucketName}")
	private String bucketName;
	@Value("${amazonProperties.accessKey}")
	private String accessKey;
	@Value("${amazonProperties.secretKey}")
	private String secretKey;
	@Value("${createdawsendpoint}")
	private String createdAwsEndpoint;

	@PostConstruct
	private void initializeAmazon() {
		AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
		this.s3client = new AmazonS3Client(credentials);
	}

	@Autowired
	private ProductRepository productRepository;

	@Override
	public ResponseEntity<?> addProduct(MultipartFile imageFile, String name, String description, String brandName,
			BigDecimal pricePerUnit, BigDecimal productWholeSalePrice, Long noOfStocks) {

		ServiceResponse response = new ServiceResponse();
		try {
			Product product = new Product();
			product.setName(name);
			product.setDescription(description);
			product.setBrandName(brandName);
			product.setPricePerUnit(pricePerUnit);
			product.setProductWholeSalePrice(productWholeSalePrice);
			product.setNoOfStocks(noOfStocks);
			String imageUrl = uploadFile(imageFile);
			product.setProductImageUrl(imageUrl);
			productRepository.save(product);
			response.setCode("200");
			response.setResponse("Product Successfully Added");
			return new ResponseEntity<ServiceResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			log.error("Internal Server Error");
			response.setCode("501");
			response.setResponse("Internal Server Error!");
			return new ResponseEntity<ServiceResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public ResponseEntity<?> getAllProducts() {
		
		ServiceResponse response = new ServiceResponse();
		try {
			List<Product> allProducts = productRepository.findAll();
			if(!(allProducts == null)) {
				return new ResponseEntity<List<Product>>(allProducts, HttpStatus.OK);
			}else {
				response.setCode("404");
				response.setResponse("No products available for shopping!");
				return new ResponseEntity<ServiceResponse>(response, HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			response.setCode("500");
			response.setResponse("Internal Server Error!");
			return new ResponseEntity<ServiceResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public String uploadFile(MultipartFile multipartFile) {

		String fileUrl = "";
		try {
			File file = convertMultiPartToFile(multipartFile);
			String fileName = generateFileName(multipartFile);
			// fileUrl = endpointUrl + "/" + bucketName + "/" + fileName;
			fileUrl = bucketName + createdAwsEndpoint + "/" + fileName;
			uploadFileTos3bucket(fileName, file);
			file.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileUrl;
	}

	private File convertMultiPartToFile(MultipartFile file) throws IOException {
		File convFile = new File(file.getOriginalFilename());
		FileOutputStream fos = new FileOutputStream(convFile);
		fos.write(file.getBytes());
		fos.close();
		return convFile;
	}

	private String generateFileName(MultipartFile multiPart) {
		return new Date().getTime() + "-" + multiPart.getOriginalFilename().replace(" ", "_");
	}

	private void uploadFileTos3bucket(String fileName, File file) {
		s3client.putObject(
				new PutObjectRequest(bucketName, fileName, file).withCannedAcl(CannedAccessControlList.PublicRead));
	}

	public String deleteFileFromS3Bucket(String fileUrl) {
		String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
		s3client.deleteObject(new DeleteObjectRequest(bucketName + "/", fileName));
		return "Successfully deleted";
	}

}
