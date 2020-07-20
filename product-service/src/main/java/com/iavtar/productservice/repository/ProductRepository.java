package com.iavtar.productservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.iavtar.productservice.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

}
