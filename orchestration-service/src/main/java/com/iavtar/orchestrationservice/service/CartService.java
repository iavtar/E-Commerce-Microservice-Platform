package com.iavtar.orchestrationservice.service;

import org.springframework.http.ResponseEntity;

import com.iavtar.orchestrationservice.dto.request.AddProductToCartRequest;
import com.iavtar.orchestrationservice.dto.request.DecreaseProductQtyRequest;
import com.iavtar.orchestrationservice.dto.request.GetCartItemRequest;
import com.iavtar.orchestrationservice.dto.request.RemoveProductFromCartRequest;

public interface CartService {

	ResponseEntity<?> addProductToCart(Long userId, AddProductToCartRequest request);

	ResponseEntity<?> decreaseProductQuantityFromCart(Long userId, DecreaseProductQtyRequest request);

	ResponseEntity<?> removeProductFromCart(Long userId, RemoveProductFromCartRequest request);

	ResponseEntity<?> getAllCartItems(Long userId, GetCartItemRequest request);


}
